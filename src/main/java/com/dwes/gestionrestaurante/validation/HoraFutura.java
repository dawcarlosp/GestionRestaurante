package com.dwes.gestionrestaurante.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HoraFuturaValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HoraFutura {
    String message() default "La hora debe ser en el futuro";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
