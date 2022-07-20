package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(final RuntimeException e) {
        log.error("Ошибка валидации: {} ", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, FilmNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final RuntimeException e) {
        log.error("Объект не найден: {} ", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final RuntimeException e) {
        log.error("Внутренняя ошибка сервера: {} ", e.getMessage());
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }
}
