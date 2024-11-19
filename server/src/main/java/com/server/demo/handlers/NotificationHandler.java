package com.server.demo.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.dtos.NotificationDTO;

@Component
public class NotificationHandler extends TextWebSocketHandler {

    List<WebSocketSession> webSocketSessions
            = Collections.synchronizedList(new ArrayList<>());
    private final Map<UUID, WebSocketSession> sessionMap = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String query = session.getUri().getQuery();
        if (query != null && query.contains("receiverId")) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("receiverId=")) {
                    String receiverId = param.split("=")[1];
                    UUID uuid = UUID.fromString(receiverId);
                    session.getAttributes().put("receiverId", receiverId);
                    sessionMap.put(uuid, session);
                    break;
                }
            }
        }
        webSocketSessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionMap.values().remove(session);
        webSocketSessions.remove(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
        for (WebSocketSession webSocketSession : webSocketSessions) {
            if (session == webSocketSession) {
                continue;
            }
            webSocketSession.sendMessage(message);
        }
    }

    public void sendMessage(UUID receiverId, List<NotificationDTO> notifications) throws Exception {
        WebSocketSession session = sessionMap.get(receiverId);
        if (session != null && session.isOpen()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String notificationsJson = objectMapper.writeValueAsString(notifications);
            session.sendMessage(new TextMessage(notificationsJson));
        }
    }
}
