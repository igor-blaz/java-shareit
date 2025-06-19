package ru.practicum.shareit.booking;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BookingTimeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBookingTime {
    String message() default "Некорректное время бронирования";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
