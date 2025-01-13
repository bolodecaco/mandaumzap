package com.server.demo.dtos;


import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições das sessões")
public class RequestSessionDTO {

    @Schema(description = "Indica se a sessão está ativa", example = "true")
    private boolean isActive;

    @Schema(description = "ID do dono do chat")
    @NotNull(message = "É preciso informar o ID do usuário")
    private UUID userId;

}
