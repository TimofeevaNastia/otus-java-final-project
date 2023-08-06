package ru.rate.config;

public class ConfigException extends RuntimeException {
    public ConfigException(Throwable cause) {
        super(cause);
    }

    public ConfigException(String message) {
        super(message);
    }
}
