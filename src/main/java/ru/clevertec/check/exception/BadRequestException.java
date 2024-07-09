package ru.clevertec.check.exception;

public class BadRequestException extends RuntimeException {

    private static final String MESSAGE = "BAD REQUEST.\nНеверные входные данные. ";

    public BadRequestException(String message) {
        super(MESSAGE + message);
    }
}
