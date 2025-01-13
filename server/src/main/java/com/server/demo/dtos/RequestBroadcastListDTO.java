package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições de lista de transmissões")
public class RequestBroadcastListDTO {

    @Schema(description = "Título da lista de transmissão", example = "Alunos do primeiro ano")
    private String title;

    @Schema(description = "ID do usuário", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID ownerId;
}
