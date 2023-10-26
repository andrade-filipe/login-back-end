package com.project.login.domain.entitys.user;

import com.project.login.domain.entitys.enums.Gender;
import com.project.login.domain.entitys.enums.UserRole;
import jakarta.persistence.*;
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

    private String name;
    private String username;
    private String email;
    private String password;

    @Enumerated(STRING)
    private Gender gender;

    private OffsetDateTime birthDate;

    @Enumerated(STRING)
    private UserRole userRole;

    private Boolean locked;
    private Boolean enabled;
}
