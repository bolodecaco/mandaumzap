package com.server.demo.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExclusiveFieldsValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExclusiveFields {

    String message() default "Apenas um dos campos 'chatId' ou 'broadcastListId' deve ser preenchido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
