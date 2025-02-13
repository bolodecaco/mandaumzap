package com.server.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições das sessões")
public class RequestSessionDTO {

    @Schema(description = "Indica se a sessão está ativa", example = "true")
    private boolean isActive;

}
