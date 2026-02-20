package com.shofiqul.socket.records;

public record SignupResponse(
        String username,
        String fullName,
        String email
) {
}
