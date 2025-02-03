package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(name = "UpdateRoutineDTO", description = "DTO para atualização de rotina")
public class UpdateRoutineCronDTO {

    @Schema(description = "ID da rotina", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID routineId;

    @Schema(description = "Cron da rotina", example = "0 0 12 * * *")
    @Pattern(regexp = "^(\\d+\\s){5}\\d+$", message = "Cron inválido")
    private String cron;
}
