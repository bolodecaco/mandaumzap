package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para retorno das sessões")
public class SessionDTO {

    @Schema(name = "id", description = "Identificador da sessão", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(name = "isActive", description = "Indica se a sessão está ativa", example = "true")
    private boolean isActive;
}
