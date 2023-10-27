package com.project.login.domain.services;

import com.project.login.domain.entitys.email.EmailSender;
import com.project.login.outside.representation.adapters.EmailSenderAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService implements EmailSender {

    private final EmailSenderAdapter emailSenderAdapter;

    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        emailSenderAdapter.sendEmail(toEmail,subject,body);
    }

}
