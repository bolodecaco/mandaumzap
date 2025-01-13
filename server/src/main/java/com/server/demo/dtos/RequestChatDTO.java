package com.server.demo.dtos;


import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições dos chats")
public class RequestChatDTO {
@Schema(description = "Nome do chat", example = "Meu Chat")
    private String chatName;

    @Schema(description = "ID do chat do WhatsApp", example = "123456789@g.us")
    private String whatsAppId;

    @Schema(description = "ID do dono do chat")
    private UUID ownerId;

    @Schema(description = "ID da sessão do chat")
    private UUID sessionId;
}
