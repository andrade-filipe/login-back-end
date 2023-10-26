package com.project.login.domain.services;

import com.project.login.domain.entitys.enums.UserRole;
import com.project.login.domain.entitys.user.User;
import com.project.login.domain.repositorys.UserRepository;
import com.project.login.outside.representation.model.input.LoginInput;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

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

    public void login(LoginInput data){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(data.username(), data.password());
        Authentication auth =
                this.authenticationManager.authenticate(token);
    }
}
