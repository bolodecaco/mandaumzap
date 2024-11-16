package com.server.demo.dtos;

import java.util.UUID;

import com.server.demo.enums.PlanType;

import lombok.Data;

@Data
public class PlanDTO {
    
    private UUID id;
    private String name;
    private String benefits;
    private double price;
    private PlanType type;

    public PlanDTO(UUID id, String name, String benefits, double price, PlanType type) {
        this.id = id;
        this.name = name;
        this.benefits = benefits;
        this.price = price;
        this.type = type;
    }
}
