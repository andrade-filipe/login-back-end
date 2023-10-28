package com.project.login.outside.representation.model.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordInput {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String newPassword;
}
