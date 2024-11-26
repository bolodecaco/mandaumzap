package com.server.demo.dtos;

import java.util.UUID;

import com.server.demo.enums.PlanType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para retorno dos planos")
public class PlanDTO {

    @Schema(name = "id", description = "ID do plano", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(name = "name", description = "Nome do plano", example = "Plano básico")
    private String name;

    @Schema(name = "benefits", description = "Benefícios do plano", example = "Acesso a 10 filmes")
    private String benefits;

    @Schema(name = "price", description = "Preço do plano", example = "10.0")
    private double price;

    @Schema(name = "type", description = "Tipo do plano", example = "basic")
    private PlanType type;

}
