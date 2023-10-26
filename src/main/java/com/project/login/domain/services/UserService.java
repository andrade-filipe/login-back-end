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
public class UserService implements UserDetailsService {

    private final String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetail userDetail =
                new UserDetail(userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException
                                        (String.format(USER_NOT_FOUND_MSG, email))
                        )
                );
        return userDetail;
    }
}
