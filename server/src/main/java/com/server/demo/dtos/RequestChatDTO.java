package com.server.demo.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições dos chats")
public class RequestChatDTO {

    @Schema(description = "Identificador do chat", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Nome do chat", example = "Meu Chat")
    private String chatName;

    @Schema(description = "Identificador do chat", example = "chat-123")
    private String chatId;

    @Schema(description = "Dono do chat")
    private PartialUserDTO owner;

}
