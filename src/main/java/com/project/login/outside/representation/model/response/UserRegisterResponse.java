package com.project.login.outside.representation.model.response;

import com.project.login.domain.entitys.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class UserRegisterResponse {

    private String name;
    private String username;
    private String email;
    private Gender gender;
    private OffsetDateTime birthDate;
}
