package com.server.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições de lista de transmissões")
public class RequestBroadcastListDTO {

    @Schema(description = "Título da lista de transmissão", example = "Alunos do primeiro ano")
    @NotEmpty(message = "A lista deve ter um título válido")
    @NotNull(message = "A lista precisa de um título")
    @Size(min = 3, max = 50, message = "O título da lista deve ter entre 3 e 50 caracteres")
    private String title;
}
