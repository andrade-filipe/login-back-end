package com.project.login.domain.services;

import com.project.login.domain.entitys.Login;
import com.project.login.domain.entitys.enums.UserRole;
import com.project.login.domain.entitys.user.User;
import com.project.login.domain.repositorys.UserRepository;
import com.project.login.infrastructure.security.TokenService;
import com.project.login.outside.representation.model.input.LoginInput;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
        if(user.getUsername().contains("ADMIN")){
            user.setUserRole(UserRole.ADMIN);
        } else {
            user.setUserRole(UserRole.USER);
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);

        user.setLocked(false);
        user.setEnabled(false);

        String body =
                "http://localhost:8080/api/v1/auth/register/"
                + user.getUsername() +
                "/confirm/"
                + tokenService.generateToken(user);

        emailSenderService.sendEmail(user.getEmail(), "Confirm your email", body);

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

        return new Login(user.getName(), token);
    }

    @Transactional
    public String confirm(String username, String token){
        if(!tokenService.validateToken(token).isEmpty()){
            User user = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Doesn't exist"));
            user.setEnabled(true);
            user.setLocked(true);
            userRepository.save(user);

            var usernamePassword = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            var auth = authenticationManager.authenticate(usernamePassword);

            return "confirm";
        }
        return "not confirmed";
    }
}
