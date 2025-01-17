package com.server.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para resposta com o qrcode")
public class QrCodeResponseDTO {

    @Schema(name = "qrcode", description = "QrCode para autenticação no whatsapp", example = "2@9D0jEYCQ+KEbKVCf68KIh1vL5D2t50wf36n7i3md3LgU/rdV3Fsp30W")
    private String qrcode;
}
