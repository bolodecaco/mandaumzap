package com.server.demo.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    @SqsListener("${cloud.aws.sqs.queue-name}")
    public void receiveMessage(String message) {
        try {
            logger.info("Mensagem recebida: {}", message);
            processMessage(message);
        } catch (Exception e) {
            logger.error("Erro ao processar mensagem: {}", e.getMessage());
            throw e;
        }
    }

    private void processMessage(String message) {
        logger.info("Processando mensagem: {}", message);
    }

    @SqsListener("${cloud.aws.sqs.queue-name}")
    public void receiveObject(Object object) {
        logger.info("Objeto recebido: {}", object);
    }
}
