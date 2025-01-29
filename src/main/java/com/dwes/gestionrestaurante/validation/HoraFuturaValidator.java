package com.dwes.gestionrestaurante.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class HoraFuturaValidator implements ConstraintValidator<HoraFutura, LocalTime> {
    @Override
    public boolean isValid(LocalTime hora, ConstraintValidatorContext context) {
        return hora != null && hora.isAfter(LocalTime.now());
    }
}
