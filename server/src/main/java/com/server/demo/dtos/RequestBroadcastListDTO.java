package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições de lista de transmissões")
public class RequestBroadcastListDTO {

    @Schema(description = "Título da lista de transmissão")
    private String title;

    @Schema(description = "Dono do chat")
    private OwnerDTO owner;

    @Data
    public static class OwnerDTO {

        @Schema(description = "ID do dono do chat", example = "123e4567-e89b-12d3-a456-426614174000")
        private String id;
    }

    @Schema(description = "ID do dono do chat (alternativa)", hidden = true)
    private UUID ownerId;

    public UUID getOwnerId() {
        return owner != null && owner.getId() != null
                ? UUID.fromString(owner.getId())
                : null;
    }

    public void setOwnerId(UUID requestOwnerId) {
        ownerId = requestOwnerId;
        if (ownerId != null) {
            this.owner = new OwnerDTO();
            this.owner.setId(ownerId.toString());
        }
    }
}
