package com.project.login.outside.application.v1.controllers;

import com.project.login.domain.entitys.email.EmailRequest;
import com.project.login.domain.exceptions.EmailServiceException;
import com.project.login.domain.services.EmailSenderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailSenderController {

    private final EmailSenderService emailSenderService;

    @PostMapping()
    public ResponseEntity sendEmail(@Valid @RequestBody EmailRequest emailRequest){
        try {
            this.emailSenderService.sendEmail(
                    emailRequest.to(),
                    emailRequest.subject(),
                    emailRequest.body());
            return ResponseEntity.ok().build();
        }catch (EmailServiceException exception){
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body("Couldn't send email");
        }
    }
}
