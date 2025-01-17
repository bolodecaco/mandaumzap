package com.server.demo.dtos;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para resposta com o qrcode")
public class BotQrCodeResponseDTO {

    @Schema(name = "id", description = "Identificador da sessão", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(name = "userId", description = "Identificador do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @Schema(name = "isActive", description = "Indica se a sessão está ativa", example = "true")
    private boolean isActive;

    @Schema(name = "qrcode", description = "QrCode para autenticação no whatsapp", example = "2@9D0jEYCQ+KEbKVCf68KIh1vL5D2t50wf36n7i3md3LgU/rdV3Fsp30W")
    private String qrcode;

}
