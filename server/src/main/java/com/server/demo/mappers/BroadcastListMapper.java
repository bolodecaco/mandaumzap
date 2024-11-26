package com.server.demo.mappers;

import org.springframework.stereotype.Service;

import com.server.demo.dtos.BroadcastListDTO;
import com.server.demo.models.BroadcastList;

@Service
public class BroadcastListMapper {

    public BroadcastListDTO toDTO(BroadcastList message) {
        return new BroadcastListDTO(
                message.getId(),
                message.getOwner().getId(),
                message.getChats(),
                message.getTitle(),
                message.getLastActiveAt(),
                message.getMessagesSent()
        );
    }
}
