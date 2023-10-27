package com.project.login.infrastructure.email;

import com.project.login.outside.representation.adapters.EmailSenderAdapter;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderConfig implements EmailSenderAdapter {

    @Override
    public void sendEmail(String toEmail, String subject, String body) {
    }

}
