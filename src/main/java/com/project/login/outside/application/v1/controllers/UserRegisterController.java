package com.project.login.outside.application.v1.controllers;

import com.project.login.domain.entitys.user.User;
import com.project.login.domain.repositorys.UserRepository;
import com.project.login.domain.services.UserRegisterService;
import com.project.login.outside.representation.mapper.UserRegisterMapper;
import com.project.login.outside.representation.model.input.UserRegisterInput;
import com.project.login.outside.representation.model.response.UserRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/auth/register")
@RequiredArgsConstructor
public class UserRegisterController {

    private final UserRepository userRepository;
    private final UserRegisterService userRegisterService;
    private final UserRegisterMapper userRegisterMapper;

    @GetMapping("/all")
    public List<UserRegisterResponse> getRegistered(){
        return userRegisterMapper.toCollectionModel(userRepository.findAll());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public UserRegisterResponse register(@Valid @RequestBody UserRegisterInput userRegisterInput){
        User newUser = userRegisterMapper.toEntity(userRegisterInput);
        User registeredUser = userRegisterService.register(newUser);
        return userRegisterMapper.toResponse(registeredUser);
    }
}
