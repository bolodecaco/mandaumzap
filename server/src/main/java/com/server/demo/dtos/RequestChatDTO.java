package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições dos chats")
public class RequestChatDTO {

    @Schema(description = "Nome do chat", example = "Meu Chat")
    @NotNull(message = "É preciso informar o nome do chat")
    @Size(min = 3, max = 50, message = "O nome do chat deve ter entre 3 e 50 caracteres")
    private String chatName;

    @Schema(description = "ID do chat do WhatsApp", example = "123456789@g.us")
    @NotBlank(message = "É preciso informar o id do chat do WhatsApp")
    @Pattern(
            regexp = "^[0-9]{1,30}@(g\\.us|s\\.whatsapp\\.net)$",
            message = "O ID do WhatsApp deve estar no formato correto, ex: 123456789@g.us ou 558496462611@s.whatsapp.net"
    )
    private String whatsAppId;

    @Schema(description = "ID da sessão do chat")
    @NotNull(message = "É preciso informar o ID da sessão do chat")
    private UUID sessionId;
}
