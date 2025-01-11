package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições das rotinas")
public class RequestRoutineDTO {
    @Schema(description = "ID do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID ownerId;

    @Schema(description = "Nome da rotina", example = "Minha Rotina")
    private String title;

    @Schema(description = "ID da mensagem", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID messageId;

    @Schema(description = "Data para ativar rotina", example = "2021-08-01T00:00:00.000Z")
    private String willActiveAt;
}
