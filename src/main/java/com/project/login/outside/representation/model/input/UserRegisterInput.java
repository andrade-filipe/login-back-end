package com.project.login.outside.representation.model.input;

import com.project.login.domain.entitys.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class UserRegisterInput {

    @NotBlank
    @Size(max = 256)
    private String name;

    @NotBlank
    @Size(max = 256)
    private String username;

    @NotBlank
    @Email
    @Size(max = 256)
    private String email;

    @NotBlank
    private String password;

    @NotNull
    @Size(max = 24)
    private Gender gender;

    @NotNull
    private OffsetDateTime birthDate;

}
