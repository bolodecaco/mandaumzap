package com.server.demo.dtos;

import java.util.Date;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições das rotinas")
public class RequestRoutineDTO {
    @Schema(description = "ID do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "É preciso informar o ID do dono da rotina")
    private UUID ownerId;

    @Schema(description = "ID da mensagem", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "É preciso informar o ID da mensagem da rotina")
    private UUID messageId;
    
    @Schema(description = "Nome da rotina", example = "Minha Rotina")
    @NotNull(message = "É preciso informar o título da rotina")
    @Size(min = 3, max = 50, message = "O título da rotina deve ter entre 3 e 50 caracteres")
    private String title;

    @Schema(description = "Data para ativar rotina", example = "2021-08-01T00:00:00.000Z")
    @NotNull(message = "É preciso informar a data de ativação da rotina")
    @Future(message = "A data de ativação da rotina deve ser no futuro")
    private Date willActiveAt;
}
