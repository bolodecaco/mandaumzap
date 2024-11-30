package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO da requisição de notificação")
public class RequestNotificationDTO {

    @Schema(name = "content", description = "Título da notificação", example = "Notificação de teste")
    private String content;

    @Schema(name = "type", description = "Descrição da notificação", example = "Essa é uma notificação de teste")
    private String type;
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
