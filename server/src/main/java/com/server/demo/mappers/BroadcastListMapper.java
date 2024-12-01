package com.server.demo.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.BroadcastListDTO;
import com.server.demo.dtos.RequestBroadcastListDTO;
import com.server.demo.models.BroadcastList;
import com.server.demo.models.User;
import com.server.demo.services.UserService;

@Service
public class BroadcastListMapper {

    @Autowired
    private UserService userService;

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

    public BroadcastList toEntity(RequestBroadcastListDTO message) {
        BroadcastList list = new BroadcastList();
        User owner = userService.findById(message.getOwnerId());
        list.setOwner(owner);
        list.setTitle(message.getTitle());
        return list;
    }
}
