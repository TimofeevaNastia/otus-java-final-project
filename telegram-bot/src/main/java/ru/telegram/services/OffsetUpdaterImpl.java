package ru.telegram.services;

import org.springframework.stereotype.Component;

@Component
public class OffsetUpdaterImpl implements OffsetUpdater {
    private long lastOffsetId = 0;

    public synchronized long getLast() {
        return lastOffsetId;
    }

    @Override
    public synchronized void setLast(long lastOffsetId) {
        this.lastOffsetId = lastOffsetId;
    }
}
