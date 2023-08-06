package ru.rate.service;

public class AddressValidationException extends RuntimeException {
    public AddressValidationException(Throwable cause) {
        super(cause);
    }

    public AddressValidationException(String message) {
        super(message);
    }
}
