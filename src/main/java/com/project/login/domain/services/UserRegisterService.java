package com.project.login.domain.services;

import com.project.login.domain.entitys.enums.UserRole;
import com.project.login.domain.entitys.user.User;
import com.project.login.domain.repositorys.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRegisterService {

    private final UserRepository userRepository;

    @Transactional
    public User register(User user) {
        user.setUserRole(UserRole.USER);
        return userRepository.save(user);
    }
}
