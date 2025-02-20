package com.server.demo.dtos;

import java.util.Date;

import com.server.demo.enums.ConnectionStatusType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para resposta com o qrcode")
public class BotDTO {

    @Schema(name = "qrcode", description = "QrCode para autenticação no whatsapp", example = "2@9D0jEYCQ+KEbKVCf68KIh1vL5D2t50wf36n7i3md3LgU/rdV3Fsp30W")
    private String qrcode;

    @Schema(name = "message", description = "Mensagem de retorno", example = "Iniciando sessão")
    private String message;

    @Schema(name = "status", description = "Status da requisição", example = "open")
    private ConnectionStatusType status;

    @Schema(name = "createdAt", description = "Data de criação", example = "2021-08-01T00:00:00.000Z")
    private Date createdAt;
}
