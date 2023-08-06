package ru.address.service;

public class ErrorFormatException extends RuntimeException {
    public ErrorFormatException(Throwable cause) {
        super(cause);
    }

    public ErrorFormatException(String message) {
        super(message);
    }
}
