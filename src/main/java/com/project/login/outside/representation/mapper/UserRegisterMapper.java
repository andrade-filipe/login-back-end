package com.project.login.outside.representation.mapper;

import com.project.login.domain.entitys.user.User;
import com.project.login.outside.representation.model.input.UserRegisterInput;
import com.project.login.outside.representation.model.response.UserRegisterResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRegisterMapper {

    private final ModelMapper modelMapper;

    public User toEntity(UserRegisterInput userRegisterInput) {
        return modelMapper.map(userRegisterInput, User.class);
    }

    public UserRegisterResponse toResponse(User user) {
        return modelMapper.map(user, UserRegisterResponse.class);
    }

    public List<UserRegisterResponse> toCollectionModel(List<User> users) {
        return users.stream()
            .map(this::toResponse)
            .toList();
    }
}
