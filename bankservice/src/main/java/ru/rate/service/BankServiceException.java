package ru.rate.service;

public class BankServiceException extends RuntimeException {
    public BankServiceException(Throwable cause) {
        super(cause);
    }

    public BankServiceException(String message) {
        super(message);
    }
}
