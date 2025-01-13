package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO da requisição de notificação")
public class RequestNotificationDTO {

    @Schema(name = "content", description = "Título da notificação", example = "Essa é uma notificação de teste")
    private String content;

    @Schema(name = "type", description = "Descrição da notificação", example = "Notificação de teste")
    private String type;

    @Schema(description = "ID do destinatário", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID receiverId;
}
