package com.server.demo.dtos;

import lombok.Data;

@Data
public class SqsMessageDTO {
    private String sessionId;
    private String status;
}
