package com.server.demo.dtos;

import com.server.demo.enums.PlanType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições dos planos")
public class RequestPlanDTO {

    @Schema(description = "Nome para o plano", example = "Plano grátis")
    private String name;

    @Schema(description = "Todos os beneífcios do plano", example = "Acesso limitado as funcionalidades")
    private String benefits;

    @Schema(description = "Custo do plano", example = "0.0")
    private double price;

    @Schema(description = "Tipo do plano", example = "FREE")
    private PlanType type;
}
