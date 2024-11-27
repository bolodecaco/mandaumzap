package com.server.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineDTO {
    private UUID id;
    private UUID ownerId;
    private String title;
    private UUID messageId;
    private Date lastActiveAt;
    private Date willActiveAt;
    private int timesSent;
}
