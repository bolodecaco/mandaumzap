package com.server.demo.dtos;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO para envio de mensagens para o bot")
public class MessageSentToBotDTO {

    @Schema(description = "Identificador da sessão", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID sessionId;

    @Schema(description = "Identificador do usuário", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID userId;

    @Schema(description = "Texto da mensagem", example = "Mensagem de exemplo")
    private String text;

    @Schema(description = "Lista de destinatários", example = "[\"5511999999999@s.whatsapp.net\",\"31636136171888171313@g.us\"]")
    private List<String> receivers;
}
