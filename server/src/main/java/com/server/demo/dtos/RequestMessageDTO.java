package com.server.demo.dtos;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para requisições de mensagens")
public class RequestMessageDTO {

    @Schema(description = "Identificador da mensagem", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Conteúdo da mensagem", example = "Mensagem de exemplo")
    private String content;

    @Schema(description = "Data da última vez que a mensagem foi enviada", example = "2024-02-20T10:30:00Z")
    private String lastSent;

    @Schema(description = "Número de vezes que a mensagem foi enviada", example = "0")
    private int timesSent;

    @Schema(description = "Identificador do chat", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID chatId;

    @Schema(description = "Identificador da lista de transmissão", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID broadcastListId;

    @Schema(description = "Dono da mensagem")
    private OwnerDTO owner;

    @Data
    public static class OwnerDTO {

        @Schema(description = "ID do dono da mensagem", example = "123e4567-e89b-12d3-a456-426614174000")
        private String id;
    }

    @JsonIgnore
    public UUID getOwnerId() {
        return owner != null && owner.getId() != null
                ? UUID.fromString(owner.getId())
                : null;
    }

    public void setOwnerId(UUID requestOwnerId) {
        if (requestOwnerId != null) {
            this.owner = new OwnerDTO();
            this.owner.setId(requestOwnerId.toString());
        }
    }
}
