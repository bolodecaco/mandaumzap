package com.server.demo.dtos;

import java.util.UUID;

import com.server.demo.validations.ExclusiveFields;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para requisições de mensagens")
@ExclusiveFields(message = "Preencha apenas 'chatId' ou 'broadcastListId', não ambos.")
public class RequestMessageDTO {

    @Schema(description = "Conteúdo da mensagem", example = "Mensagem de exemplo")
    @NotEmpty(message = "O conteúdo da mensagem não pode ser vazio")
    private String content;

    @Schema(description = "Identificador do chat a ser enviado", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID chatId;

    @Schema(description = "Identificador da lista de transmissão", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID broadcastListId;

    @Schema(description = "ID do usuário que irá enviar a mensagem", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull(message = "O ID do usuário não pode ser nulo")
    private UUID ownerId;
}
