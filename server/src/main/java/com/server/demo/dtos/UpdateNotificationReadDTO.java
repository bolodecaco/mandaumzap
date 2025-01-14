package com.server.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO da requisição de atualização de notificação")
public class UpdateNotificationReadDTO {

    @Schema(name = "read", description = "Se a notificação foi lida", example = "true")
    @NotNull(message = "É preciso informar o status de leitura da notificação")
    private boolean read;
}
