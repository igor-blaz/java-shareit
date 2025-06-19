package ru.practicum.shareit.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleValidationException(DuplicateEmailException e) {
        return new ErrorResponse(e.getMessage(), "Ошибка. Такой Email уже зарегистрирован");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTimeValidationException(TimeValidationException e) {
        return new ErrorResponse(e.getMessage(), "Время указано неверно");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleItemUnavailableException(ItemUnavailableException e) {
        return new ErrorResponse(e.getMessage(), "Эта вещь недоступна");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse(e.getMessage(), "Не найдено/ 404");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIllegalAccessDeniedException(AccessDeniedException e) {
        return new ErrorResponse(e.getMessage(), "Ошибка");
    }


}
