package ru.stepup.access.log.parser;

import java.time.LocalDateTime;

public class Statistics {
    private int totalTraffic;
    private int entryCount;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;


    public Statistics() {
        this.totalTraffic = 0;
        this.entryCount = 0;
        this.minTime = null;
        this.maxTime = null;
    }

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getDataSize();

        LocalDateTime entryTime = entry.getTimestamp();
        if (minTime == null || entryTime.isBefore(minTime)) {
            minTime = entryTime;
        }
        if (maxTime == null || entryTime.isAfter(maxTime)) {
            maxTime = entryTime;
        }
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null || minTime.isEqual(maxTime)) {
            return 0;
        }
        long hoursDifference = java.time.Duration.between(minTime, maxTime).toHours();
        return (double) totalTraffic / hoursDifference;
    }
}
