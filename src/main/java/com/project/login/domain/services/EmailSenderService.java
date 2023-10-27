package com.project.login.domain.services;

import com.project.login.domain.entitys.email.EmailSender;
import com.project.login.domain.exceptions.EmailServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService implements EmailSender {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String toEmail, String subject, String body) throws EmailServiceException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("filipeandrade.work@gmail.com");
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        }catch (EmailServiceException exception){
            throw new EmailServiceException("Couldn't send the email");
        }

    }

}
