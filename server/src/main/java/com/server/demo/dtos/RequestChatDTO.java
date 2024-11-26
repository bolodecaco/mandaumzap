package com.server.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições dos chats")
public class RequestChatDTO {

    @Schema(name = "chatId", description = "Identificador do chat")
    private String chatId;

    public String getChatId() {
        return chatId;
    }
}
