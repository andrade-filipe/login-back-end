package com.project.login.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailSender Service Test")
class EmailSenderServiceTest {
    @Captor
    ArgumentCaptor<SimpleMailMessage> captor;
    @Mock
    JavaMailSender mailSender;
    @InjectMocks
    EmailSenderService emailSenderService;

    @Nested
    @DisplayName("Method: sendEmail -> @param String, String, String")
    class SendEmailMethodTest {
        @Nested
        @DisplayName("GIVEN a valid toEmail, subject and body")
        class GivenValidParams {
            String toEmail = "dev.filipeandrade@gmail.com";
            String subject = "Test Email";
            String body = "This is a test";

            @Nested
            @DisplayName("WHEN sending an email")
            class WhenSendingEmail {
                @BeforeEach
                void sendEmailTest() {
                    emailSenderService.sendEmail(toEmail, subject, body);
                }

                @Nested
                @DisplayName("ASSERT that")
                class AssertThat {
                    @BeforeEach
                    void verifyMailSender() {
                        verify(mailSender).send(captor.capture());
                    }

                    @Test
                    @DisplayName("Email is being sent to the recipient")
                    void emailSenderRecipient() {
                        assertThat
                            (Arrays.stream(Objects.requireNonNull(captor.getValue().getTo()))
                                .findFirst()).isNotEmpty().isEqualTo(Optional.of(toEmail));
                    }

                    @Test
                    @DisplayName("Email has the right message")
                    void emailSenderText() {
                        assertThat(
                            captor.getValue().getText())
                            .isEqualTo(body);
                    }

                    @Test
                    @DisplayName("Email has the right subject")
                    void emailSenderSubject() {
                        assertThat(
                            captor.getValue().getSubject())
                            .isEqualTo(subject);
                    }
                }
            }
        }
    }
}
