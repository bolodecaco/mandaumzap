package com.server.demo.producer;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.dtos.MessageDTO;

@Service
public class MessageProducer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AmazonSQS amazonSQS;

    @Value("${cloud.aws.sqs.queue-url}")
    private String queueUrl;

    public void sendMessage(String message) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message)
                .withMessageGroupId("default")
                .withMessageDeduplicationId(UUID.randomUUID().toString());

        amazonSQS.sendMessage(sendMessageRequest);
    }

    public void sendObject(MessageDTO messageSentToBotDTO) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(messageSentToBotDTO);
            SendMessageRequest sendMessageRequest = new SendMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMessageBody(jsonMessage)
                    .withMessageGroupId(messageSentToBotDTO.getId().toString())
                    .withMessageDeduplicationId(UUID.randomUUID().toString());
            amazonSQS.sendMessage(sendMessageRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar o objeto para JSON", e);
        }
    }
}
