package ru.clevertec.check.exception;

public class NotEnoughMoneyException extends RuntimeException {

    private  static final String MESSAGE = "NOT ENOUGH MONEY.\nНедостаточно средств.";

    public NotEnoughMoneyException() {
        super(MESSAGE);
    }
}
