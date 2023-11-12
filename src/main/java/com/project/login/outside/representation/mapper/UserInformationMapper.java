package com.project.login.outside.representation.mapper;

import com.project.login.domain.entitys.user.User;
import com.project.login.outside.representation.model.response.UserInformationResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserInformationMapper {
    private final ModelMapper modelMapper;

    public UserInformationResponse toResponse(Optional<User> user){
        return modelMapper.map(user, UserInformationResponse.class);
    }
}
