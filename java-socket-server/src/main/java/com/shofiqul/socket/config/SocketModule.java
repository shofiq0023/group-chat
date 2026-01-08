package com.shofiqul.socket.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.shofiqul.socket.dto.MessageDto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketModule {
    private final Logger log = LoggerFactory.getLogger(SocketModule.class);

    @Autowired
    private SocketIOServer server;

    @PostConstruct
    public void startServer() {
        server.start();
        addEventListener();
        log.info("Socket server started!");
    }

    @PreDestroy
    public void stopServer() {
        server.stop();
    }

    public void addEventListener() {
        server.addConnectListener(client -> {
            client.sendEvent("connect", "Socket connected!");
        });

        server.addDisconnectListener(client -> {
            log.info("{} disconnected", client.getSessionId());
        });

        server.addEventListener("message", MessageDto.class, (client, message, request) -> {
            client.sendEvent("message", message);
        });
    }
}
