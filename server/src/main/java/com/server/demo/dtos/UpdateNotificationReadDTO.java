package com.server.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO da requisição de atualização de notificação")
public class UpdateNotificationReadDTO {

    @Schema(name = "read", description = "Se a notificação foi lida", example = "true")
    private boolean read;
}
