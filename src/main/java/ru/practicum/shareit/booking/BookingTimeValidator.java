package ru.practicum.shareit.booking;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BookingTimeValidator implements ConstraintValidator<ValidBookingTime, Booking> {

    @Override
    public boolean isValid(Booking booking, ConstraintValidatorContext context) {


        if (booking.getStart().isAfter(booking.getEnd())) {
            return false;
        } else return !booking.getStart().isEqual(booking.getEnd());
    }
}






