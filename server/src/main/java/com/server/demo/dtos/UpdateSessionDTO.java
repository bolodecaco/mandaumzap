package com.server.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO da requisição de atualização de atividade da sessão")
public class UpdateSessionDTO {
    
    @Schema(description = "Indica se a sessão está ativa", example = "true")
    private boolean active;
}
