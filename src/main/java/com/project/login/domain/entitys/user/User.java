package com.project.login.domain.entitys.user;

import com.project.login.domain.entitys.enums.Gender;
import com.project.login.domain.entitys.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.UUID;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = UUID)
    @EqualsAndHashCode.Include
    private String userId;

    @NotBlank
    @Size(max = 256)
    private String name;

    @NotBlank
    private String username;

    @Email
    @Size(max = 256)
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotNull
    @Enumerated(STRING)
    private Gender gender;

    @NotNull
    private OffsetDateTime birthDate;

    @NotNull
    @Enumerated(STRING)
    private UserRole userRole;

    @NotNull
    private Boolean locked;

    @NotNull
    private Boolean enabled;
}
