package com.project.login.outside.representation.model.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginInput(@NotBlank @Size(max = 256) String login, @NotBlank String password) {
}
