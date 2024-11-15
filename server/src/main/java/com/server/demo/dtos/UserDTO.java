package com.server.demo.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class UserDTO {

    private UUID id;
    private String name;
    private String phone;
    private String avatar;

    public UserDTO(UUID id, String name, String phone, String avatar) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
    }
}
