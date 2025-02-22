package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para requisições de mensagens")
public class RequestMessageDTO {

    @Schema(description = "Conteúdo da mensagem", example = "Mensagem de exemplo")
    @NotBlank(message = "O conteúdo da mensagem não pode ser vazio")
    @Size(min = 3, max = 10000, message = "O conteúdo da mensagem deve ter entre 3 e 10.000 caracteres")
    private String content;

    @Schema(description = "Identificador da lista de transmissão", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull(message = "É preciso informar o ID da lista de transmissão")
    private UUID broadcastListId;

    @Schema(description = "ID da sessão que irá enviar a mensagem", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull(message = "É preciso informar o ID da sessão de envio")
    private UUID sessionId;

    @Schema(description = "URL da mensagem", example = "https://avatars.githubusercontent.com/u/105653717?v=4")
    @Size(max = 1000, message = "A URL da mensagem deve ter no máximo 1.000 caracteres")
    private String url;
}
