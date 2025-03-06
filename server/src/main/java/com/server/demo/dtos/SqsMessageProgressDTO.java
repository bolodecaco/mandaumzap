package com.server.demo.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class SqsMessageProgressDTO {

    private int sentChats;
    private int unsentChats;
    private int totalChats;
    private UUID messageId;
    private String userId;
}
