package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO da requisição de notificação")
public class RequestNotificationDTO {

    @Schema(name = "content", description = "Descrição da notificação", example = "Essa é uma notificação de teste")
    @NotNull(message = "É preciso informar o conteúdo da notificação")
    @Size(min = 3, max = 256, message = "O conteúdo da notificação deve ter entre 3 e 256 caracteres")
    private String content;

    @Schema(name = "type", description = "Tipo da notificação", example = "Notificação de teste")
    @NotNull(message = "É preciso informar o tipo da notificação")
    @Size(min = 3, max = 50, message = "O tipo da notificação deve ter entre 3 e 50 caracteres")
    private String type;

    @Schema(description = "ID do destinatário", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "É preciso informar o ID do destinatário da notificação")
    private UUID receiverId;
    
}
