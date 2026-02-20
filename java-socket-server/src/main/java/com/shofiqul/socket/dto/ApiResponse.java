package com.shofiqul.socket.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private T data;
    private String message;
    private boolean success;

    @Builder.Default
    private Instant timestamp = Instant.now();

    public static <T> ApiResponse<T> ok(T data, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .success(true)
                .build();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .message("Success")
                .success(true)
                .build();
    }

    public static <T> ApiResponse<T> error(T data, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .success(false)
                .build();
    }

    public static <T> ApiResponse<T> error(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .message("Error, Something went wrong!")
                .success(false)
                .build();
    }
}
