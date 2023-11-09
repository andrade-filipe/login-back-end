package com.project.login.domain.entitys.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface EmailSender {
    void sendEmail(@Email String toEmail, @NotBlank String subject, @NotBlank String body);
}
