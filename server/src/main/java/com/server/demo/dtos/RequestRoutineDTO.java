package com.server.demo.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.server.demo.enums.FrequencyType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para requisições das rotinas")
public class RequestRoutineDTO {

    @Schema(description = "ID do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "É preciso informar o ID do dono da rotina")
    private UUID ownerId;

    @Schema(description = "ID da mensagem", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "É preciso informar o ID da mensagem da rotina")
    private UUID messageId;

    @Schema(description = "Nome da rotina", example = "Minha Rotina")
    @NotNull(message = "É preciso informar o título da rotina")
    @Size(min = 3, max = 50, message = "O título da rotina deve ter entre 3 e 50 caracteres")
    private String title;

    @Schema(description = "Descrição da rotina", example = "HH:mm")
    @NotNull(message = "É preciso informar a hora de execução da rotina")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "O horário de execução da rotina deve estar no formato HH:mm")
    private LocalDateTime executionDateTime;

    @Schema(description = "Dias da semana", example = "MON,WED,FRI")
    @Pattern(regexp = "^(?:MON|TUE|WED|THU|FRI|SAT|SUN)(?:,(?:MON|TUE|WED|THU|FRI|SAT|SUN))*$", message = "Os dias da semana devem estar no formato MON,TUE,WED,THU,FRI,SAT,SUN")
    private String daysOfWeek;

    @Schema(description = "Dia do mês", example = "1")
    private Integer dayOfMonth;

    @Schema(description = "Frequência da rotina", example = "DAILY")
    @Pattern(regexp = "^(?:DAILY|WEEKLY|MONTHLY)$", message = "A frequência da rotina deve ser DAILY, WEEKLY ou MONTHLY")
    private FrequencyType frequency;
}
