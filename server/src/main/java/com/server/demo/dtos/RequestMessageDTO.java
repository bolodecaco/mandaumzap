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

    @Schema(name = "messageId", description = "Identificador da mensagem")
    private UUID chatRecipientId;

    @Schema(name = "message", description = "Mensagem")
    private UUID broadcastListId;
}
