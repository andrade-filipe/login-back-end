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

    private final String USER_EMAIL_NOT_FOUND_MSG = "user with email %s not found";
    private final String USER_USERNAME_NOT_FOUND_MSG = "user with username %s not found";
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserDetail userDetailResponse;
        if(userRepository.findByUsername(login).isPresent()){
            UserDetail userDetail = new UserDetail(userRepository
                    .findByUsername(login)
                    .orElseThrow(() ->
                            new UsernameNotFoundException
                                    (String.format(USER_USERNAME_NOT_FOUND_MSG, login))));
            userDetailResponse = userDetail;
        } else if (userRepository.findByEmail(login).isPresent()) {
            UserDetail userDetail = new UserDetail(userRepository
                    .findByEmail(login)
                    .orElseThrow(() ->
                            new UsernameNotFoundException
                                    (String.format(USER_EMAIL_NOT_FOUND_MSG, login))));
            userDetailResponse = userDetail;
        } else {
            throw new UsernameNotFoundException("User Not Found");
        }

        return userDetailResponse;
    }
}
