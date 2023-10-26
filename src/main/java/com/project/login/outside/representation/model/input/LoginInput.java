package com.project.login.outside.representation.model.input;

import jakarta.validation.constraints.NotBlank;

public record LoginInput(
        @NotBlank String login,
        @NotBlank String password) { }
