package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para requisições de mensagens")
public class RequestMessageDTO {

    @Schema(name = "id", description = "Identificador da mensagem")
    private UUID id;

    @Schema(name = "content", description = "Conteúdo da mensagem")
    private String content;

    @Schema(name = "lastSent", description = "Data da última vez que a mensagem foi enviada")
    private String lastSent;

    @Schema(name = "timesSent", description = "Número de vezes que a mensagem foi enviada")
    private int timesSent;

    @Schema(name = "ownerId", description = "Identificador do dono da mensagem")
    private UUID ownerId;

    @Schema(name = "chatId", description = "Identificador do chat")
    private UUID chatId;

    @Schema(name = "message", description = "Mensagem")
    private UUID broadcastListId;
}
