package com.server.demo.dtos;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "BotChatsDTO", description = "Dto para retornos dos chats")
public class BotChatsDTO {

    @Schema(description = "Quantidade de chats", example = "1")
    private int count;

    @Schema(description = "Lista de chats", example = "[{\"id\": \"123@g.us\", \"name\": \"Chat 1\"}]")
    private List<BotResponseChats> chats;

    @Data
    public static class BotResponseChats {

        private String id;

        private String name;
    }
}
