package com.project.login.outside.representation.model.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordInput {
    @NotBlank
    @Size(max = 256)
    private String username;

    @NotBlank
    private String newPassword;
}
