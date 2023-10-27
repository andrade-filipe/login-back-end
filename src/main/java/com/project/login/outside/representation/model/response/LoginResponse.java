package com.project.login.outside.representation.model.response;

import com.project.login.domain.entitys.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginResponse {
    private String name;
    private UserRole role;
    private String token;
}
