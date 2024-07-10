package ru.clevertec.check.exception;

public class NoPathToSaveFileException extends BadRequestException {

    private static final String MESSAGE = "\nНе указан путь для сохранения данных чека.";

    public NoPathToSaveFileException() {
        super(MESSAGE);
    }
}
