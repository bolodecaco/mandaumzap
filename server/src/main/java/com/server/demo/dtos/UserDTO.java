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
@Schema(description = "DTO para retorno dos notificações")
public class UserDTO {

    @Schema(name = "id", description = "Identificador do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(name = "name", description = "Nome do usuário", example = "João da Silva")
    private String name;

    @Schema(name = "phone", description = "Telefone do usuário", example = "11999999999")
    private String phone;

    @Schema(name = "avatar", description = "Avatar do usuário", example = "https://www.google.com/avatar.jpg")
    private String avatar;

    @Schema(name = "plan", description = "Plano do usuário", example = "FREE")
    private PlanType plan;

}
