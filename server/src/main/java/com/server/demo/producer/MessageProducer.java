package com.server.demo.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.MessageSentToBotDTO;

@Service
public class MessageProducer {

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Value("${cloud.aws.sqs.queue-url}")
    private String queueUrl;

    public void sendMessage(String message) {
        queueMessagingTemplate.send(queueUrl,
                MessageBuilder.withPayload(message).build());
    }

    public void sendObject(MessageSentToBotDTO messageSentToBotDTO) {
        queueMessagingTemplate.convertAndSend(queueUrl, messageSentToBotDTO);
    }
}
