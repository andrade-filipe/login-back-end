package com.project.login.domain.services;

import com.project.login.domain.entitys.Login;
import com.project.login.domain.entitys.enums.UserRole;
import com.project.login.domain.entitys.user.User;
import com.project.login.domain.repositorys.UserRepository;
import com.project.login.infrastructure.security.TokenService;
import com.project.login.outside.representation.model.input.LoginInput;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final EmailSenderService emailSenderService;

    @Transactional
    public User register(User user) {
        String token = tokenService.generateToken(user);

        if(user.getUsername().contains("ADMIN")){
            user.setUserRole(UserRole.ADMIN);
        } else {
            user.setUserRole(UserRole.USER);
        }

        user.setLocked(false);
        user.setEnabled(false);

        String body = "http://localhost:8080/api/v1/auth/register/confirm?username=" + user.getUsername() + "&token=" + token;

        emailSenderService.sendEmail(user.getEmail(), "Confirm your email", body);

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    @Transactional
    public Login login(LoginInput data){
        String token;
        User user = userRepository
                .findByUsernameOrEmail(data.login(), data.login())
                .orElseThrow(NoSuchElementException::new);

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        if(auth.isAuthenticated()){
            token = tokenService.generateToken(user);
        } else {
            throw new RuntimeException("Is not authenticated");
        }

        return new Login(user.getName(), user.getUserRole(), token);
    }

    //    @Transactional
    //    public Login loginOAuth(LoginInput data){
    //        return new Login(null,null,null);
    //  }

    @Transactional
    public Login confirm(String username, String token){
            User user = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Doesn't exist"));

            user.setEnabled(true);
            user.setLocked(true);
            userRepository.save(user);

            return new Login(user.getName(), user.getUserRole(), token);
    }

    @Transactional
    public void changePassword(String email, String newPassword){
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Doesn't exist"));

        String encryptedPassword = new BCryptPasswordEncoder().encode(newPassword);
        user.setPassword(encryptedPassword);

        userRepository.save(user);
    }
}
