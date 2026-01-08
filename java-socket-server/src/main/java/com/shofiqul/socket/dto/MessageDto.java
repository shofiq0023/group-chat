package com.shofiqul.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String userId;
    private String message;
    private long timestamp;

    public MessageDto(String userId, String message) {
        this.userId = userId;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}
