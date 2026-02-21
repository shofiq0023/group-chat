package com.shofiqul.socket.records;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "User handle cannot be blank")
        String userHandle,

        @NotBlank(message = "Password cannot be blank")
        String password
) {
}
