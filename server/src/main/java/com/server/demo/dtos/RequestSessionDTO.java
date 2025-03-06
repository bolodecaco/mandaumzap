package com.server.demo.dtos;

import com.server.demo.enums.ConnectionStatusType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições das sessões")
public class RequestSessionDTO {

    @Schema(description = "Indica se a sessão está ativa", example = "pending")
    private ConnectionStatusType status;

}
