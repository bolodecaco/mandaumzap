package com.server.demo.validations;

import com.server.demo.dtos.RequestMessageDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.UUID;

public class ExclusiveFieldsValidator implements ConstraintValidator<ExclusiveFields, RequestMessageDTO> {

    @Override
    public boolean isValid(RequestMessageDTO dto, ConstraintValidatorContext context) {
        UUID chatId = dto.getChatId();
        UUID broadcastListId = dto.getBroadcastListId();

        return (chatId != null && broadcastListId == null) || (chatId == null && broadcastListId != null);
    }
}
