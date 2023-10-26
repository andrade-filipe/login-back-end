package com.project.login.outside.representation.model.input;

import com.project.login.domain.entitys.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class UserRegisterInput {

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private Gender gender;

    @NotNull
    private OffsetDateTime birthDate;

}
