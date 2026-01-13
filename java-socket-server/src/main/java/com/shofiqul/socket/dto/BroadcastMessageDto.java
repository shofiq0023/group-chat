package com.shofiqul.socket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BroadcastMessageDto {
    private String fromUsername;
    private String message;
    private Timestamp timestamp;
}
