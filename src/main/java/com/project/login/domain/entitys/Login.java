package com.project.login.domain.entitys;

import com.project.login.domain.entitys.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Login {
    private String name;
    private String username;
    private UserRole role;
    private String token;
}
