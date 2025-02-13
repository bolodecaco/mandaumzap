package com.server.demo.dtos;

import java.util.Date;
import java.util.UUID;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para retorna da lista de transmissão")
public class BroadcastListDTO {
    @Schema(description = "ID da lista de transmissão")
    private UUID id;

    @Schema(description = "Título da lista de transmissão")
    private String title;

    @Schema(description = "A última mensagem enviada foi em")
    private Date lastActiveAt;

    @Schema(description = "Quantidade de mensagens enviadas")
    private int messagesSent;
}
