package com.server.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições dos chats")
public class RequestUserDTO {

    @Schema(description = "Senha do usuário", example = "123456")
    private String password;

    @Schema(description = "Email do usuário", example = "jhondoe@example.com")
    private String email;

    @Schema(description = "Nome do usuário", example = "Jhon Doe")
    private String name;

    @Schema(description = "Telefone do usuário", example = "+55 11 99999-9999")
    private String phone;

    @Schema(description = "Avatar do usuário", example = "https://example.com/avatar.jpg")
    private String avatar;

}
