package com.server.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para requisição de novas sessoões para o Bot")
public class BotRequestDTO {

    @Schema(name = "sessionId", description = "Identificador da sessão", example = "123e4567-e89b-12d3-a456-426614174000")
    private String sessionId;

    @Schema(name = "userId", description = "Identificador do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    private String userId;
}