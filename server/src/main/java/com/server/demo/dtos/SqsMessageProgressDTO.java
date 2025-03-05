package com.server.demo.dtos;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class SqsMessageProgressDTO {

    private UUID messageId;
    private String userId;
    private int sentChats;
    private int totalChats;
    private int unsentChats;
    private String sessionId;
    private String text;
    private List<String> receivers;
    private String url;

}
