package com.shofiqul.socket.records;

public record LoginResponse(
        Long userId,
        String username,
        String token
) {
}
