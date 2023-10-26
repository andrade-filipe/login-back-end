package com.project.login.domain.services;

import com.project.login.domain.entitys.Login;
import com.project.login.domain.entitys.enums.UserRole;
import com.project.login.domain.entitys.user.User;
import com.project.login.domain.repositorys.UserRepository;
import com.project.login.infrastructure.security.TokenService;
import com.project.login.outside.representation.model.input.LoginInput;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Transactional
    public User register(User user) {
        if(user.getUsername().contains("ADMIN")){
            user.setUserRole(UserRole.ADMIN);
        } else {
            user.setUserRole(UserRole.USER);
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }

    @Transactional
    public Login login(LoginInput data){
        User user;
        String token;

        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);

        if(userRepository.findByUsername(data.login()).isPresent()){
            user = userRepository.findByUsername(data.login()).get();
        } else if(userRepository.findByEmail(data.login()).isPresent()) {
            user = userRepository.findByEmail(data.login()).get();
        } else {
            throw new NoSuchElementException("Could not find User");
        }

        if(auth.isAuthenticated()){
            token = tokenService.generateToken(user);
        } else {
            throw new RuntimeException("Is not authenticated");
        }
        return new Login(user.getName(), token);
    }
}
