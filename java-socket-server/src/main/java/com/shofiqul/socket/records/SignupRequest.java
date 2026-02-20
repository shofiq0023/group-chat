package com.shofiqul.socket.records;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank(message = "Full name cannot be blank")
        String fullName,

        @NotBlank(message = "Username cannot be blank")
        String username,

        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password cannot be blank")
        String password
) {
}
