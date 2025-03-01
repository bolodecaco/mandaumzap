package com.server.demo.dtos;

import com.server.demo.enums.PlanType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "DTO para atualização dos planos")
public class UpdatePlanDTO {
    @Schema(description = "Nome para o plano", example = "Plano grátis")
    @NotBlank(message = "O nome do plano não pode estar vazio")
    private String name;

    @Schema(description = "Todos os benefícios do plano", example = "Acesso limitado às funcionalidades")
    @NotBlank(message = "Os benefícios do plano não podem estar vazios")
    private String benefits;

    @Schema(description = "Custo do plano", example = "0.0")
    @Min(value = 0, message = "O preço do plano não pode ser negativo")
    private double price;

    @Schema(description = "Tipo do plano", example = "FREE")
    @NotNull(message = "O tipo do plano não pode ser nulo")
    private PlanType type;
}
