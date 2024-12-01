package com.server.demo.mappers;

import org.springframework.stereotype.Service;

import com.server.demo.dtos.RequestUserDTO;
import com.server.demo.dtos.UserDTO;
import com.server.demo.models.User;

@Service
public class UserMapper {

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getAvatar(),
                user.getPlan().getType()
        );
    }

    public User toEntity(RequestUserDTO chat) {
        User currentUser = new User();
        currentUser.setPassword(chat.getPassword());
        currentUser.setEmail(chat.getEmail());
        currentUser.setName(chat.getName());
        currentUser.setPhone(chat.getPhone());
        currentUser.setAvatar(chat.getAvatar());
        return currentUser;
    }
}
