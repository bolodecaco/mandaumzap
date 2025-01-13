package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para retorno do ID do usuário")
public class PartialUserDTO {
    @Schema(name = "id", description = "Identificador do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
}
