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
    @GeneratedValue(strategy = IDENTITY)
    private Long userId;

    private String name;
    private String username;
    private String email;
    private String password;
    private Gender gender;
    private OffsetDateTime birthDate;

    @Enumerated(STRING)
    private UserRole userRole;

    private Boolean locked;
    private Boolean enabled;

    public User(String name,
                String username,
                String email,
                String password,
                UserRole userRole,
                Boolean locked,
                Boolean enabled) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.locked = locked;
        this.enabled = enabled;
    }
}
