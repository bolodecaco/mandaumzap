package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para resposta com qrcode, mensagem e status")
public class BotConnectionDTO {

    @Schema(name = "id", description = "ID da sessão", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(name = "qrcode", description = "QrCode para autenticação no whatsapp", example = "2@9D0jEYCQ+KEbKVCf68KIh1vL5D2t50wf36n7i3md3LgU/rdV3Fsp30W")
    private String qrcode;

    @Schema(name = "message", description = "Mensagem de retorno", example = "Iniciando sessão")
    private String message;

    @Schema(name = "status", description = "Status da requisição", example = "open")
    private String status;
}
