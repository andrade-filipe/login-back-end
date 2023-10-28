package com.project.login.domain.entitys.user;

import com.project.login.domain.entitys.enums.Gender;
import com.project.login.domain.entitys.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.OffsetDateTime;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@Entity
public class User{

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
