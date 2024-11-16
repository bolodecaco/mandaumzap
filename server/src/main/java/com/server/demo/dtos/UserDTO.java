package com.server.demo.dtos;

import java.util.UUID;

import com.server.demo.models.Plan;

import lombok.Data;

@Data
public class UserDTO {

    private UUID id;
    private String name;
    private String phone;
    private String avatar;
    private Plan plan;

    public UserDTO(UUID id, String name, String phone, String avatar, Plan plan) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
        this.plan = plan;
    }
}
