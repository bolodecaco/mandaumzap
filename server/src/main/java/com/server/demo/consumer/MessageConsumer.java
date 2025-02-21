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
import com.server.demo.dtos.SqsMessageDTO;

@Service
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @Autowired
    private AmazonSQS amazonSQS;

    @Value("${cloud.aws.sqs.queue-status-name}")
    private String queueName;

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
                processMessage(sqsMessageParse);
                amazonSQS.deleteMessage(queueUrl, message.getReceiptHandle());
            } catch (Exception e) {
                logger.error("Erro ao processar mensagem: {}", e.getMessage());

            }
        }
    }

    private void processMessage(SqsMessageDTO message) {
        logger.info("Processando mensagem: {}", message);
    }
}
