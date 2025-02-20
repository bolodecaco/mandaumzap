package com.server.demo.consumer;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.dtos.BotChatsDTO;
import com.server.demo.dtos.SqsMessageDTO;
import com.server.demo.enums.ConnectionStatusType;
import com.server.demo.exception.BusinessException;
import com.server.demo.models.Session;
import com.server.demo.repositories.SessionRepository;
import com.server.demo.services.ChatService;
import com.server.demo.services.SessionService;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Data
public class MessageConsumer {

    @Value("${cloud.aws.sqs.queue-name}")
    private String queueName;

    @Value("${bot.whatsapp.url}")
    private String botUrl;

    @Value("${bot.whatsapp.token}")
    private String botToken;

    @Autowired
    private final AmazonSQS amazonSQSClient;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private final SessionService sessionService;

    @Autowired
    private final SessionRepository sessionRepository;

    @Autowired
    private final ChatService chatService;

    @Scheduled(fixedDelay = 2000)
    public void receiveMessage() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String queueUrl = amazonSQSClient.getQueueUrl(queueName).getQueueUrl();
            ReceiveMessageResult receiveMessageResult = amazonSQSClient.receiveMessage(queueUrl);
            if (receiveMessageResult.getMessages().isEmpty()) {
                return;
            }
            Message message = receiveMessageResult.getMessages().get(0);
            SqsMessageDTO sqsMessage = objectMapper.readValue(message.getBody(), SqsMessageDTO.class);
            processMessage(sqsMessage);
            amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
        } catch (Exception e) {
            log.error("Queue Exception Message: {}", e.getMessage());
        }
    }

    private void processMessage(SqsMessageDTO sqsMessage) {
        switch (sqsMessage.getType()) {
            case "connection-status":
                try {
                    UUID sessionId = UUID.fromString(sqsMessage.getSessionId());
                    ConnectionStatusType status = ConnectionStatusType.valueOf(sqsMessage.getStatus().toLowerCase());
                    log.info("Connection status: SessionId {}, Status {}", sessionId, status);
                    sessionService.setConnectionStatus(sessionId, status);
                    if (status.equals(ConnectionStatusType.open)) {
                        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new BusinessException("Sessão não encontrada"));
                        List<BotChatsDTO.ResponseChat> chats = requestChats(sessionId, session.getUserId());
                        chatService.insertChats(chats, sessionId);
                    }
                } catch (IllegalArgumentException e) {
                    log.error("Invalid status received: {}", sqsMessage.getStatus());
                }
                break;
            case "progress":
                log.info("Progress: Sent {}, Unsent {}, Total {}",
                        sqsMessage.getSentChats(),
                        sqsMessage.getUnsentChats(),
                        sqsMessage.getTotalChats());
                break;
            default:
                log.warn("Unknown message type: {}", sqsMessage.getType());
        }
    }

    private List<BotChatsDTO.ResponseChat> requestChats(UUID sessionId, String userId) {
        return webClientBuilder
                .baseUrl(botUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder
                .path("/api/chats/{userId}/{sessionId}")
                .queryParam("token", botToken)
                .build(userId, sessionId.toString()))
                .retrieve()
                .bodyToMono(BotChatsDTO.class)
                .block()
                .getChats();
    }

}
