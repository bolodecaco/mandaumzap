package com.server.demo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.dtos.NotificationDTO;
import com.server.demo.events.NotificationEvent;
import com.server.demo.handlers.NotificationHandler;
import com.server.demo.services.NotificationService;

import java.io.IOException;
import java.util.List;

@Component
public class NotificationEventListener {

    private static final Logger logger = LoggerFactory.getLogger(NotificationEventListener.class);

    @Autowired
    private NotificationHandler notificationHandler;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;

    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        String receiverId = event.getReceiverId();

        try {
            List<NotificationDTO> notifications = notificationService.findUnreadByReceiverId(receiverId);
            WebSocketSession session = notificationHandler.getSession(receiverId);

            if (session != null && session.isOpen()) {
                String notificationsJson = objectMapper.writeValueAsString(notifications);
                session.sendMessage(new TextMessage(notificationsJson));
                logger.info("Notificações enviadas para o usuário: {}", receiverId);
            } else {
                logger.warn("Sessão WebSocket não encontrada ou fechada para o usuário: {}", receiverId);
            }
        } catch (IOException e) {
            logger.error("Erro ao enviar notificação via WebSocket: {}", e.getMessage());
        }
    }
}
