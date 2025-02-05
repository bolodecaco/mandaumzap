package com.server.demo.dtos;

import java.util.Date;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para informações da Rotina")
public class RoutineDTO {

    @Schema(description = "Identificador único da rotina", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
        
    @Schema(description = "Título da rotina", example = "Lembrete Diário")
    private String title;

    @Schema(description = "ID da mensagem associada", example = "123e4567-e89b-12d3-a456-426614174002")
    private UUID messageId;

    @Schema(description = "Última vez que a rotina esteve ativa")
    private Date lastActiveAt;

    @Schema(description = "Próxima data de ativação programada")
    private String cron;

    @Schema(description = "Número de vezes que a rotina foi executada", example = "5")
    private int timesSent;
}
