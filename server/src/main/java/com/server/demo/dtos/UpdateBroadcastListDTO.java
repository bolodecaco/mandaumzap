package com.server.demo.dtos;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para atualização da lista de transmissão")
public class UpdateBroadcastListDTO {
    @Schema(description = "Título da lista de transmissão", example = "Minha Lista de Transmissão")
    private String title;
}
