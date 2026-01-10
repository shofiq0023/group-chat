package com.shofiqul.socket.config;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.shofiqul.socket.dto.MessageDto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, SocketIOClient> users = new HashMap<>();

        server.addConnectListener(client -> {
            client.sendEvent("connected", "Socket connected!");
            String username = client.getHandshakeData().getSingleUrlParam("username");
            users.put(username, client);
            log.info("{} client connected!", client.getSessionId());
        });

        server.addDisconnectListener(client -> log.info("{} disconnected", client.getSessionId()));

        server.addEventListener("message", MessageDto.class, (client, message, request) -> {
            SocketIOClient toClient = users.get(message.getToUsername());
            toClient.sendEvent("message", message);
        });
    }
}
