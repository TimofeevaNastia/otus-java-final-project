package ru.telegram.services;

public interface OffsetUpdater {
    long getLast();

    void setLast(long lastUpdateId);
}
