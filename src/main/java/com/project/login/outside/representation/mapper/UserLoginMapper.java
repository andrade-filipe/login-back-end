package com.project.login.outside.representation.mapper;

import com.project.login.domain.entitys.Login;
import com.project.login.outside.representation.model.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLoginMapper {

    private final ModelMapper modelMapper;

    public LoginResponse toResponse(Login login){
        return modelMapper.map(login, LoginResponse.class);
    }
}
