package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para retorno dos chats")
public class ChatDTO {

    @Schema(name = "id", description = "ID do chat", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(name = "chatId", description = "ID do chat", example = "123e4567-e89b-12d3-a456-426614174000")
    private String chatId;

    @Schema(name = "chatName", description = "Nome do chat", example = "Chat do João")
    private String chatName;

    @Schema(name = "ownerId", description = "ID do dono do chat", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID ownerId;

    @Schema(name = "sessionId", description = "Identificador da sessão", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID sessionId;
}
