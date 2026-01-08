package com.shofiqul.socket.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfig {
    @Value("${socket.host}")
    private String socketHost;

    @Value("${socket.port}")
    private Integer socketPort;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(socketHost);
        config.setPort(socketPort);
        config.setOrigin("*");
        config.setMaxFramePayloadLength(1024*1024);
        config.setMaxHttpContentLength(1024*1024);

        return new SocketIOServer(config);
    }
}
