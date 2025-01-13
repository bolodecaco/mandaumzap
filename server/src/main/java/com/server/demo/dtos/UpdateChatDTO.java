package com.server.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para atualização dos chats")
public class UpdateChatDTO {
    @Schema(description = "Nome do chat", example = "Meu Chat")
    private String chatName;

    @Schema(description = "ID do chat do WhatsApp", example = "123456789@g.us")
    private String whatsAppId;
}
