package ru.address.service;

public class AddresstException extends RuntimeException {
    public AddresstException(Throwable cause) {
        super(cause);
    }

    public AddresstException(String message) {
        super(message);
    }
}
