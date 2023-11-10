package com.project.login.domain.services;

import com.project.login.domain.entitys.Login;
import com.project.login.domain.entitys.enums.Gender;
import com.project.login.domain.entitys.enums.UserRole;
import com.project.login.domain.entitys.user.User;
import com.project.login.domain.entitys.user.User.UserBuilder;
import com.project.login.domain.exceptions.UserAuthServiceException;
import com.project.login.domain.repositorys.UserRepository;
import com.project.login.infrastructure.security.TokenService;
import com.project.login.outside.representation.model.input.LoginInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.OffsetDateTime;
import java.util.Optional;

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
    @Spy
    UserBuilder validUserBuilder = builder().userId("uuid1").name("Filipe").username("filipe")
        .email("filipeandrade@gmail.com").password("1234").gender(Gender.MALE)
        .birthDate(OffsetDateTime.now()).userRole(UserRole.USER).enabled(true).locked(true);

    @Nested
    @DisplayName("Method: register() -> @param User")
    class RegisterMethodTests {
        @Nested
        @DisplayName("GIVEN a valid User to register")
        class GivenValidUser {
            @Spy
            UserBuilder userRegisterBuilder = builder()
                .name("Filipe")
                .username("filipe")
                .email("filipeandrade@gmail.com")
                .password("1234")
                .gender(Gender.MALE)
                .birthDate(OffsetDateTime.now());
            @Spy
            UserBuilder adminRegisterBuilder = builder()
                .name("Filipe")
                .username("ADMIN.filipe")
                .email("filipeandrade@gmail.com")
                .password("1234")
                .gender(Gender.MALE)
                .birthDate(OffsetDateTime.now());

            @BeforeEach
            void mockResponses() {
                when(bCryptPasswordEncoder.encode(any()))
                    .thenReturn("encrypted");
            }

            @Nested
            @DisplayName("WHEN registering User")
            class WhenRegisteringUser {
                User user = userRegisterBuilder.build();

                @BeforeEach
                void registering() {
                    userAuthService.register(user);
                }

                @Nested
                @DisplayName("ASSERT THAT")
                class AssertThat {
                    @Test
                    @DisplayName("is adding the user role")
                    void addUserRole() {
                        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
                    }

                    @Test
                    @DisplayName("is setting the locked and enabled states to false")
                    void lockedAndEnabled() {
                        assertThat(user.getLocked()).isEqualTo(false);
                        assertThat(user.getEnabled()).isEqualTo(false);
                    }

                    @Test
                    @DisplayName("is encrypting the password")
                    void encryption() {
                        assertThat(user.getPassword()).isEqualTo("encrypted");
                    }

                    @Test
                    @DisplayName("password is being encrypted before save")
                    void order() {
                        InOrder inOrder = inOrder(bCryptPasswordEncoder, userRepository);
                        inOrder.verify(bCryptPasswordEncoder, times(1))
                            .encode(any(String.class));
                        inOrder.verify(userRepository, times(1))
                            .save(any(User.class));
                    }
                }
            }

            @Nested
            @DisplayName("WHEN registering ADMIN")
            class WhenRegisteringAdmin {
                User admin = adminRegisterBuilder.build();

                @BeforeEach
                void registeringAdmin() {
                    userAuthService.register(admin);
                }

                @Nested
                @DisplayName("ASSERT THAT")
                class AssertThat {
                    @Test
                    @DisplayName("IS setting UserRole to ADMIN")
                    void isAdmin() {
                        assertThat(admin.getUserRole()).isEqualTo(UserRole.ADMIN);
                    }
                }
            }
        }

        @Nested
        @DisplayName("Given a Null user WHEN registering")
        class GivenInvalidUser {
            @Nested
            @DisplayName("ASSERT THAT")
            class AssertThat {
                @Test
                @DisplayName("IS throwing UserAuthServiceException and not saving")
                void exception() {
                    assertThatThrownBy(() -> userAuthService.register(null))
                        .isInstanceOf(UserAuthServiceException.class)
                        .hasMessage("User is Empty");
                    verify(userRepository, never()).save(any(User.class));
                }
            }
        }
    }

    @Nested
    @DisplayName("Method: login() -> @param LoginInput, @return Login")
    class LoginMethodTests {
        LoginInput validLoginInput = new LoginInput("filipe", "1234");
        LoginInput invalidLoginInput = new LoginInput("joão", "1234");
        User userLogged = validUserBuilder.build();
        @Mock
        Authentication auth;

        @Nested
        @DisplayName("GIVEN VALID login information")
        class GivenValidLogin {
            Login validLoginReturn;

            @BeforeEach
            void mockResponses() {
                when(userRepository.findByUsernameOrEmail(eq("filipe"), eq("filipe")))
                    .thenReturn(Optional.of(userLogged));

                when(authenticationManager.authenticate(any()))
                    .thenAnswer(invocationOnMock -> auth);

                when(tokenService.generateToken(any())).thenReturn("token");

                when(auth.isAuthenticated())
                    .thenReturn(true);
            }

            @Nested
            @DisplayName("WHEN logging in")
            class WhenLoggingIn {
                @BeforeEach
                void doLogin() {
                    validLoginReturn = userAuthService.login(validLoginInput);
                }

                @Nested
                @DisplayName("ASSERT THAT")
                class AssertThat {
                    @Test
                    @DisplayName("User is being found by the repository")
                    void findUser() {
                        verify(userRepository, times(1))
                            .findByUsernameOrEmail(any(), any());
                    }

                    @Test
                    @DisplayName("User is being authenticated")
                    void isBeingAuthenticated() {
                        verify(authenticationManager, times(1))
                            .authenticate(any());
                    }

                    @Test
                    @DisplayName("Token is being generated")
                    void generateToken() {
                        verify(tokenService, times(1))
                            .generateToken(userLogged);
                    }

                    @Test
                    @DisplayName("is returning a Login instance with correct information")
                    void loginInstance() {
                        assertThat(validLoginReturn).isInstanceOf(Login.class);
                        assertThat(validLoginReturn.getToken()).isEqualTo("token");
                        assertThat(validLoginReturn.getName()).isEqualTo("Filipe");
                        assertThat(validLoginReturn.getRole()).isEqualTo(UserRole.USER);
                    }

                    @Test
                    @DisplayName("is calling the methods in the right order")
                    void rightOrder() {
                        InOrder inOrder = inOrder(userRepository, authenticationManager, tokenService);
                        inOrder.verify(userRepository, times(1)).findByUsernameOrEmail(any(), any());
                        inOrder.verify(authenticationManager, times(1)).authenticate(any());
                        inOrder.verify(tokenService, times(1)).generateToken(any());
                    }
                }
            }
        }

        @Nested
        @DisplayName("GIVEN Invalid login information")
        class GivenInvalidLogin {
            @Nested
            @DisplayName("When logging in")
            class WhenLoggingIn {
                @BeforeEach
                void exception(){
                    when(userRepository.findByUsernameOrEmail(any(), any()))
                        .thenThrow(new UsernameNotFoundException("User Doesn't exist"));
                }
                @Nested
                @DisplayName("ASSERT THAT")
                class AssertThat {
                    @Test
                    @DisplayName("throws an exception if user wrong")
                    void exceptionUser() {
                        assertThatThrownBy(() -> userAuthService.login(invalidLoginInput))
                            .isInstanceOf(UsernameNotFoundException.class)
                            .hasMessage("User Doesn't exist");
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("Method: confirmEmail() -> @param String, String, @return Login")
    class ConfirmEmailMethodTests {

        @Nested
        @DisplayName("GIVEN username and token")
        class Given {
            User user = validUserBuilder.locked(false).enabled(false).build();
            String username = user.getUsername();
            String token = "token";
            @BeforeEach
            void mockResponses(){
                when(userRepository.findByUsername(any(String.class)))
                    .thenReturn(Optional.of(user));
            }
            @Nested
            @DisplayName("WHEN confirming email")
            class When {
                Login login;
                @BeforeEach
                void callMethod(){
                    login = userAuthService.confirmEmail(username, token);
                }
                @Nested
                @DisplayName("ASSERT THAT")
                class Assert {
                    @Test
                    @DisplayName("Locked and Enabled states are being changed")
                    void stateChanged(){
                        assertThat(user.getLocked()).isEqualTo(true);
                        assertThat(user.getEnabled()).isEqualTo(true);
                    }

                    @Test
                    @DisplayName("its returning the right Login for the user")
                    void rightLogin(){
                        assertThat(login.getName()).isEqualTo("Filipe");
                        assertThat(login.getRole()).isEqualTo(UserRole.USER);
                        assertThat(login.getToken()).isEqualTo("token");
                    }

                    @Test
                    @DisplayName("its returning an instance of login")
                    void loginInstance(){
                        assertThat(login).isInstanceOf(Login.class);
                    }

                    @Test
                    @DisplayName("verify if its updating the user")
                    void updating(){
                        verify(userRepository, times(1))
                            .save(any(User.class));
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("Method: changePassword() -> @param String, String")
    class ChangePasswordMethodTests {
        User user = validUserBuilder.build();
        String newPassword = "newPass";
        @Nested
        @DisplayName("GIVEN valid user")
        class Given {
            @BeforeEach
            void mockResponses(){
                when(userRepository.findByEmail(any(String.class)))
                    .thenReturn(Optional.of(user));
                when(bCryptPasswordEncoder.encode(any(String.class)))
                    .thenReturn("encryptedPassword");
            }
            @Nested
            @DisplayName("WHEN changing password")
            class When {
                @BeforeEach
                void callMethod(){
                    userAuthService.changePassword(user.getEmail(), newPassword);
                }
                @Nested
                @DisplayName("ASSERT THAT")
                class Assert {
                    @Test
                    @DisplayName("Repository was called and found user")
                    void repositoryCall(){
                        verify(userRepository, times(1))
                            .findByEmail(any(String.class));
                    }

                    @Test
                    @DisplayName("BCrypt was called and encrypted password")
                    void bcryptCall(){
                        verify(bCryptPasswordEncoder, times(1))
                            .encode(any(String.class));
                        assertThat(user.getPassword()).isEqualTo("encryptedPassword");
                    }

                    @Test
                    @DisplayName("Repository updated user")
                    void repositoryUpdate(){
                        verify(userRepository,times(1))
                            .save(any(User.class));
                    }
                }
            }
        }
    }
}




