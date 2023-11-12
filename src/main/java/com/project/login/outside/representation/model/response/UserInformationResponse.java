package com.project.login.outside.representation.model.response;

import com.project.login.domain.entitys.enums.Gender;
import com.project.login.domain.entitys.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class UserInformationResponse {
    private String name;
    private String username;
    private String email;
    private Gender gender;
    private OffsetDateTime birthDate;
    private UserRole role;
}
