package com.server.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições de lista de transmissões")
public class RequestBroadcastListDTO {

    @Schema(description = "Título da lista de transmissão")
    private String title;

    @Schema(description = "Dono do chat")
    private PartialUserDTO owner;
}
