package com.server.demo.dtos;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de resposta do bot com o chat")
public class BotChatsDTO {

    @Schema(description = "Quantidade total de chats", example = "312")
    private int count;

    @Schema(description = "Lista de chats")
    private List<ResponseChat> chats;

    @Data
    @Schema(description = "Informações de um chat")
    public static class ResponseChat {
        
        @Schema(description = "ID do chat", example = "558412345678@s.whatsapp.net")
        private String id;
        
        @Schema(description = "Nome do chat", example = "Bladisson da Silva")
        private String name;
    }
}
