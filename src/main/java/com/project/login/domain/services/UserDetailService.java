package com.project.login.domain.services;

import com.project.login.domain.entitys.user.UserDetail;
import com.project.login.domain.repositorys.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final String USER_LOGIN_NOT_FOUND_MSG = "user with login %s not found";
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserDetail userDetailResponse =
            new UserDetail(userRepository
                .findByUsernameOrEmail(login, login)
                .orElseThrow(() ->
                    new UsernameNotFoundException
                        (String.format("User detail: " + USER_LOGIN_NOT_FOUND_MSG, login))));
        return userDetailResponse;
    }
}
