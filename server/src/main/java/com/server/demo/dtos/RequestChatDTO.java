package com.server.demo.dtos;

import lombok.Data;

@Data
public class RequestChatDTO {

    private String chatId;

    public String getChatId() {
        return chatId;
    }
}
