package com.shofiqul.socket.config;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.shofiqul.socket.dto.BroadcastMessageDto;
import com.shofiqul.socket.dto.MessageDto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketModule {
    private final Logger log = LoggerFactory.getLogger(SocketModule.class);
    private final String ROOM_BROADCAST = "rm_broadcast";

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
        Map<String, SocketIOClient> users = new ConcurrentHashMap<>();

        server.addConnectListener(client -> {
            client.sendEvent("connected", "Socket connected!");
            String username = client.getHandshakeData().getSingleUrlParam("username");
            users.put(username, client);
            client.joinRoom(ROOM_BROADCAST);

            log.info("{} client connected!", client.getSessionId());
            log.info("{} client joined the room: {}", client.getSessionId(), ROOM_BROADCAST);
        });

        server.addDisconnectListener(client -> log.info("{} disconnected", client.getSessionId()));

        server.addEventListener("message", MessageDto.class, (client, message, request) -> {
            SocketIOClient toClient = users.get(message.getToUsername());
            toClient.sendEvent("message", message);
        });

        server.addEventListener("broadcast_message", BroadcastMessageDto.class, (client, message, request) -> {
            log.debug("message: {}", message);
            server.getRoomOperations(ROOM_BROADCAST).sendEvent("broadcast_message", message);
        });
    }
}
