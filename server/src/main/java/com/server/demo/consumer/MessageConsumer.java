package com.server.demo.consumer;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.dtos.BotChatsDTO;
import com.server.demo.dtos.SqsMessageDTO;
import com.server.demo.enums.ConnectionStatusType;
import com.server.demo.exception.BusinessException;
import com.server.demo.models.Session;
import com.server.demo.repositories.SessionRepository;
import com.server.demo.services.ChatService;

@Service
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${cloud.aws.sqs.queue-status-name}")
    private String queueName;

    @Value("${bot.whatsapp.url}")
    private String botUrl;

    @Value("${bot.whatsapp.token}")
    private String botToken;

    @Scheduled(fixedDelay = 5000)
    public void pollQueue() {
        ObjectMapper objectMapper = new ObjectMapper();
        String queueUrl = amazonSQS.getQueueUrl(queueName).getQueueUrl();
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                .withMaxNumberOfMessages(10)
                .withWaitTimeSeconds(5);

        List<Message> messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();

        for (Message message : messages) {
            try {
                SqsMessageDTO sqsMessageParse = objectMapper.readValue(message.getBody(), SqsMessageDTO.class);
                amazonSQS.deleteMessage(queueUrl, message.getReceiptHandle());
                UUID sessionId = UUID.fromString(sqsMessageParse.getSessionId());
                ConnectionStatusType status = ConnectionStatusType.valueOf(sqsMessageParse.getStatus());
                Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new BusinessException("Sessão não encontrada"));
                session.setStatus(status);
                sessionRepository.save(session);
                if (status.equals(ConnectionStatusType.open)) {
                    List<BotChatsDTO.BotResponseChats> chats = processMessage(session, status);
                    chatService.insertChats(chats, sessionId);
                }
            } catch (Exception e) {
                logger.error("Erro ao processar mensagem: {}", e.getMessage());

            }
        }
    }

    private List<BotChatsDTO.BotResponseChats> processMessage(Session session, ConnectionStatusType status) {
        String userId = session.getUserId();
        return requestChats(session.getId(), userId);
    }

    private List<BotChatsDTO.BotResponseChats> requestChats(UUID sessionId, String userId) {
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
