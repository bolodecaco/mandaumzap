package com.server.demo.consumer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.dtos.SqsMessageProgressDTO;
import com.server.demo.models.Notification;
import com.server.demo.services.NotificationService;

@Service
public class MessageProgressConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private AmazonSQS amazonSQS;

    @Value("${cloud.aws.sqs.queue-name}")
    private String queueName;

    @Autowired
    private NotificationService notificationService;

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
                SqsMessageProgressDTO sqsMessageParse = objectMapper.readValue(message.getBody(), SqsMessageProgressDTO.class);
                amazonSQS.deleteMessage(queueUrl, message.getReceiptHandle());
                if (sqsMessageParse.getSessionId() != null) {
                    return;
                }
                Notification notification = new Notification();
                notification.setId(sqsMessageParse.getMessageId());
                notification.setReceiverId(sqsMessageParse.getUserId());
                notification.setContent(objectMapper.writeValueAsString(sqsMessageParse));
                notification.setType("progress");
                notification.setRead(false);
                notificationService.createNotification(notification);
            } catch (Exception e) {
                logger.error("Erro ao processar mensagem: {}", e.getMessage());

            }
        }
    }

}
