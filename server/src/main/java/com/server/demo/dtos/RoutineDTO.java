package com.server.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para informações da Rotina", example = """
        {
          "id": "123e4567-e89b-12d3-a456-426614174000",
          "ownerId": "123e4567-e89b-12d3-a456-426614174001",
          "title": "Lembrete de Reunião Diária",
          "messageId": "123e4567-e89b-12d3-a456-426614174002",
          "lastActiveAt": "2024-01-20T10:30:00Z",
          "willActiveAt": "2024-01-21T10:30:00Z",
          "timesSent": 5
        }
        """)
public class RoutineDTO {
    @Schema(description = "Identificador único da rotina", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;
    
    @Schema(description = "ID do proprietário da rotina", example = "123e4567-e89b-12d3-a456-426614174001")
    private UUID ownerId;
    
    @Schema(description = "Título da rotina", example = "Lembrete Diário")
    private String title;
    
    @Schema(description = "ID da mensagem associada", example = "123e4567-e89b-12d3-a456-426614174002")
    private UUID messageId;
    
    @Schema(description = "Última vez que a rotina esteve ativa")
    private Date lastActiveAt;
    
    @Schema(description = "Próxima data de ativação programada")
    private Date willActiveAt;
    
    @Schema(description = "Número de vezes que a rotina foi executada", example = "5")
    private int timesSent;
}
