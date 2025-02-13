package com.server.demo.handlers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.server.demo.events.ConnectionEstablishedEvent;

@Component
public class NotificationHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessionMap = Collections.synchronizedMap(new HashMap<>());

    private final ApplicationEventPublisher eventPublisher;

    public NotificationHandler(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String query = session.getUri().getQuery();
        if (query != null && query.contains("receiverId")) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("receiverId=")) {
                    String receiverId = param.split("=")[1];
                    session.getAttributes().put("receiverId", receiverId);
                    sessionMap.put(receiverId, session);

                    eventPublisher.publishEvent(new ConnectionEstablishedEvent(this, receiverId));
                    break;
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessionMap.values().remove(session);
    }

    public WebSocketSession getSession(String receiverId) {
        return sessionMap.get(receiverId);
    }
}
