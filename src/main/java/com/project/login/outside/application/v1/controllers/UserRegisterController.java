package com.project.login.outside.application.v1.controllers;

import com.project.login.domain.entitys.user.User;
import com.project.login.domain.services.UserRegisterService;
import com.project.login.outside.representation.mapper.UserRegisterMapper;
import com.project.login.outside.representation.model.input.UserRegisterInput;
import com.project.login.outside.representation.model.response.UserRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/register")
@RequiredArgsConstructor
public class UserRegisterController {

    private final UserRegisterService userRegisterService;
    private final UserRegisterMapper userRegisterMapper;

    @PostMapping
    @ResponseStatus(CREATED)
    public UserRegisterResponse register(@Valid @RequestBody UserRegisterInput registerInput){
        User newUser = userRegisterMapper.toEntity(registerInput);
        User registeredUser = userRegisterService.register(newUser);
        return userRegisterMapper.toResponse(registeredUser);
    }
}
