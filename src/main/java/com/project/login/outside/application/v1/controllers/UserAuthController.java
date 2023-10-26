package com.project.login.outside.application.v1.controllers;

import com.project.login.domain.entitys.user.User;
import com.project.login.domain.repositorys.UserRepository;
import com.project.login.domain.services.UserAuthService;
import com.project.login.outside.representation.mapper.UserRegisterMapper;
import com.project.login.outside.representation.model.input.LoginInput;
import com.project.login.outside.representation.model.input.UserRegisterInput;
import com.project.login.outside.representation.model.response.UserRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserRepository userRepository;
    private final UserAuthService userAuthService;
    private final UserRegisterMapper userRegisterMapper;


    @GetMapping("/all")
    public List<UserRegisterResponse> getRegistered(){
        return userRegisterMapper.toCollectionModel(userRepository.findAll());
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginInput data){
        userAuthService.login(data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public UserRegisterResponse register(@Valid @RequestBody UserRegisterInput userRegisterInput){
        User newUser = userRegisterMapper.toEntity(userRegisterInput);
        User registeredUser = userAuthService.register(newUser);
        return userRegisterMapper.toResponse(registeredUser);
    }
}