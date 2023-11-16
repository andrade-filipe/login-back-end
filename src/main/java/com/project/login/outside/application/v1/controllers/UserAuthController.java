package com.project.login.outside.application.v1.controllers;

import com.project.login.domain.entitys.user.User;
import com.project.login.domain.services.UserAuthService;
import com.project.login.outside.representation.mapper.UserLoginMapper;
import com.project.login.outside.representation.mapper.UserRegisterMapper;
import com.project.login.outside.representation.model.input.ChangePasswordInput;
import com.project.login.outside.representation.model.input.ForgotPasswordInput;
import com.project.login.outside.representation.model.input.LoginInput;
import com.project.login.outside.representation.model.input.UserRegisterInput;
import com.project.login.outside.representation.model.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;
    private final UserRegisterMapper userRegisterMapper;
    private final UserLoginMapper userLoginMapper;

    @PostMapping("/login")
    @ResponseStatus(OK)
    public LoginResponse login(@Valid @RequestBody LoginInput data) {
        return userLoginMapper.toResponse(userAuthService.login(data));
    }

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public void register(@Valid @RequestBody UserRegisterInput userRegisterInput) {
        User newUser = userRegisterMapper.toEntity(userRegisterInput);
        userAuthService.register(newUser);
    }

    @GetMapping("/register/confirm")
    @ResponseStatus(OK)
    public LoginResponse confirm(@RequestParam("username") String username) {
        return userLoginMapper.toResponse(userAuthService.confirmEmail(username));
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(OK)
    public void forgotPassword(@Valid @RequestBody ForgotPasswordInput forgotPassword){
        userAuthService.forgotPassword(forgotPassword);
    }

    @PostMapping("/change-password")
    @ResponseStatus(OK)
    public void changePassword(@Valid @RequestBody ChangePasswordInput changePasswordInput) {
        userAuthService.changePassword(
            changePasswordInput.getUsername(),
            changePasswordInput.getNewPassword()
        );
    }
}
