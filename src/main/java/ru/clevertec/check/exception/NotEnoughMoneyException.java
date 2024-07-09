package ru.clevertec.check.exception;

public class NotEnoughMoneyException extends RuntimeException {

    private  static final String MESSAGE = "ERROR\nNOT ENOUGH MONEY.\nНедостаточно средств.";

    public NotEnoughMoneyException() {
        super(MESSAGE);
    }
}
