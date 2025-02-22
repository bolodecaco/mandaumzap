package com.server.demo.dtos;

import java.util.Date;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para retorno das mensagens")
public class MessageDTO {

    @Schema(name = "id", description = "ID da mensagem", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(name = "content", description = "Conteúdo da mensagem", example = "Olá, tudo bem?")
    private String content;

    @Schema(name = "sentAt", description = "Data de envio da mensagem", example = "2021-10-01T00:00:00.000Z")
    private Date lastSent;

    @Schema(name = "timesSent", description = "Número de vezes que a mensagem foi enviada", example = "1")
    private int timesSent;

    @Schema(name = "sessionId", description = "ID da sessão da mensagem enviada", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID sessionId;

    @Schema(name = "broadcastListId", description = "ID da lista de transmissão", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID broadcastListId;

    @Schema(name = "url", description = "URL da mensagem", example = "https://avatars.githubusercontent.com/u/105653717?v=4")
    private String url;

}
