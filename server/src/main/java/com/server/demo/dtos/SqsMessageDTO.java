package com.server.demo.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SqsMessageDTO {
    private String type;
    private String status;
    private String sessionId;
    private Integer sentChats;
    private Integer unsentChats;
    private Integer totalChats;
}
