package com.project.login.domain.services;

import com.project.login.domain.entitys.enums.Gender;
import com.project.login.domain.entitys.enums.UserRole;
import com.project.login.domain.entitys.user.User;
import com.project.login.domain.entitys.user.User.UserBuilder;
import com.project.login.domain.exceptions.UserAuthServiceException;
import com.project.login.domain.repositorys.UserRepository;
import com.project.login.infrastructure.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.OffsetDateTime;

import static com.project.login.domain.entitys.user.User.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("UserAuthService Tests")
@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {
    @Captor
    ArgumentCaptor<User> userCaptor;
    @Mock
    UserRepository userRepository;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    TokenService tokenService;
    @Mock
    EmailSenderService emailSenderService;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    UserAuthService userAuthService;

    @Nested
    @DisplayName("Method: register() -> @param User")
    class RegisterMethodTests {
        @Nested
        @DisplayName("GIVEN a valid User to register")
        class GivenValidUser {
            @Spy
            UserBuilder userBuilder = builder()
                .name("Filipe")
                .username("lipe")
                .email("filipeandrade@gmail.com")
                .password("1234")
                .gender(Gender.MALE)
                .birthDate(OffsetDateTime.now());

            @BeforeEach
            void mockResponses(){
                when(bCryptPasswordEncoder.encode(any()))
                    .thenReturn("encrypted");
            }
            @Nested
            @DisplayName("WHEN registering")
            class WhenRegistering {
                User user = userBuilder.build();
                @BeforeEach
                void registering(){
                    userAuthService.register(user);
                }
                @Nested
                @DisplayName("ASSERT THAT")
                class AssertThat {
                    @Test
                    @DisplayName("is adding the user role")
                    void addUserRole(){
                        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
                    }
                    @Test
                    @DisplayName("is setting the locked and enabled states to false")
                    void lockedAndEnabled(){
                        assertThat(user.getLocked()).isEqualTo(false);
                        assertThat(user.getEnabled()).isEqualTo(false);
                    }
                    @Test
                    @DisplayName("is encrypting the password")
                    void encryption(){
                        assertThat(user.getPassword()).isEqualTo("encrypted");
                    }
                }
            }
        }
        @Nested
        @DisplayName("Given a Null user WHEN registering")
        class GivenInvalidUser{
            @Nested
            @DisplayName("ASSERT THAT")
            class AssertThat{
                @Test
                @DisplayName("IS throwing UserAuthServiceException")
                void exception(){
                    assertThatThrownBy(() -> userAuthService.register(null))
                        .isInstanceOf(UserAuthServiceException.class)
                        .hasMessage("User is Empty");
                }
            }
        }
    }

    @Nested
    @DisplayName("Method: login() -> @param LoginInput, @return Login")
    class LoginMethodTests {
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

    @Nested
    @DisplayName("Method: confirmEmail() -> @param String, String, @return Login")
    class ConfirmEmailMethodTests {
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

    @Nested
    @DisplayName("Method: changePassword() -> @param String, String")
    class ChangePasswordMethodTests {
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
