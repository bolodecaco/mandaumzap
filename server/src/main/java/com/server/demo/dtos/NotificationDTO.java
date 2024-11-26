package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para retorno dos notificações")
public class NotificationDTO {

    @Schema(name = "id", description = "ID da notificação", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(name = "content", description = "Conteúdo da notificação", example = "Olá, tudo bem?")
    private String content;

    @Schema(name = "read", description = "Se a notificação foi lida", example = "true")
    private boolean read;

    @Schema(name = "type", description = "Tipo da notificação", example = "message")
    private String type;

    @Schema(name = "receiverId", description = "ID do destino da notificação", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID receiverId;

}
