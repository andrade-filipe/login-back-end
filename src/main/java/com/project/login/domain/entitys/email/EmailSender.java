package com.project.login.domain.entitys.email;

public interface EmailSender {
    void sendEmail(String toEmail, String subject, String body);
}
