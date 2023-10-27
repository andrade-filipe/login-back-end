package com.project.login.outside.application.v1.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
public class UserController {

    @GetMapping
    public String home(){
        return "Home";
    }
    @GetMapping("/user")
    public String user(){
        return "Hello, User!";
    }

    @GetMapping("/admin")
    public String admin(){
        return "Hello, Admin!";
    }

    //    @GetMapping("/all")
//    public List<UserRegisterResponse> getRegistered(){
//        return userRegisterMapper.toCollectionModel(userRepository.findAll());
//    }
}
