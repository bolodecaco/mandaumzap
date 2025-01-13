package com.server.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO da requisição de atualização de atividade da sessão")
public class UpdateSessionDTO {
    
    @Schema(description = "Indica se a sessão está ativa", example = "true")
    @NotNull(message = "É preciso informar se a sessão está ativa ou não")
    private boolean active;
}
