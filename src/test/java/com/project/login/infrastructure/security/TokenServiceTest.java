package com.project.login.infrastructure.security;

import com.auth0.jwt.JWT;
import com.project.login.domain.entitys.enums.Gender;
import com.project.login.domain.entitys.enums.UserRole;
import com.project.login.domain.entitys.user.User;
import com.project.login.domain.entitys.user.User.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.project.login.domain.entitys.user.User.builder;
import static org.mockito.Mockito.when;

@DisplayName("class: TokenService -> Tests")
@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    TokenService tokenService;

    @Nested
    @DisplayName("method: generateToken(), @param User, @return String")
    class GenerateTokenMethod {
        @Nested
        @DisplayName("GIVEN any user")
        class Given {
            @Spy
            UserBuilder validUserBuilder = builder().userId("uuid1").name("Filipe").username("filipe")
                .email("filipeandrade@gmail.com").password("1234").gender(Gender.MALE)
                .birthDate(OffsetDateTime.now()).userRole(UserRole.USER).enabled(true).locked(true);
            User user = validUserBuilder.build();

            @BeforeEach
            void responses() {
                when(JWT.create()).then(invocationOnMock -> "token");
            }

            @Nested
            @DisplayName("WHEN generating JWT token")
            class When {
                @BeforeEach
                void callMethod() {
                    tokenService.generateToken(user);
                }

                @Nested
                @DisplayName("ASSERT THAT")
                class Assert {

                }
            }
        }
    }

    @Nested
    @DisplayName("method: validateToken(), @param String, @return String")
    class ValidateTokenMethod {
        @Nested
        @DisplayName("GIVEN")
        class Given {
            @Nested
            @DisplayName("WHEN")
            class When {
                @Nested
                @DisplayName("ASSERT THAT")
                class Assert {
                }
            }
        }
    }
}
