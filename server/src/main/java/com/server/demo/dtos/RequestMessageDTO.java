package com.server.demo.dtos;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor 
@AllArgsConstructor
public class RequestMessageDTO {
    private UUID chatRecipientId;
    private UUID broadcastListId; 
}
