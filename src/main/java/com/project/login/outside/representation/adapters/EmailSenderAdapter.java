package com.project.login.outside.representation.adapters;

public interface EmailSenderAdapter {
    void sendEmail(String toEmail, String subject, String body);
}
