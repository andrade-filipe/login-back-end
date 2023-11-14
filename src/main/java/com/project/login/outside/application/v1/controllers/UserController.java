package com.project.login.outside.application.v1.controllers;

import com.project.login.domain.repositorys.UserRepository;
import com.project.login.outside.representation.mapper.UserInformationMapper;
import com.project.login.outside.representation.model.response.UserInformationResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserRepository userRepository;
    private final UserInformationMapper userInformationMapper;

    @GetMapping
    public String home() {
        return "Home";
    }

    @GetMapping("/user")
    public String user() {
        return "Hello, User!";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Hello, Admin!";
    }

    @GetMapping("/information")
    public UserInformationResponse userInformation(@RequestParam("username") String username){
        return userInformationMapper.toResponse(userRepository.findByUsername(username));
    }
}
