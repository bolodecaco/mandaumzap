package com.server.demo.dtos;


import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "DTO para adicionar chat à lista de transmissão")
public class AddChatToBroadcastListDTO {
    @Schema(description = "ID do chat à ser adicionado", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull(message = "O ID do chat é obrigatório")
    @Pattern(regexp="^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$", message = "O ID do chat não é um UUID válido")
    private UUID chatId;
}
