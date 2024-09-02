package ru.stepup.access.log.parser;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {
    private int totalTraffic;
    private int entryCount;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> pages = new HashSet();
    private HashMap<String, Integer> osCountMap = new HashMap();
    private int allEntries = 0;

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getDataSize();
        pages.add(String.valueOf(entry.getResponseCode()));
        userAgentMap(entry.getUserAgent());
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

    public void userAgentMap(UserAgent userAgent) {
        if (userAgent.getOs().equals("Windows") || userAgent.getOs().equals("macOS") || userAgent.getOs().equals("Linux")) {
            osCountMap.put(userAgent.getOs(), osCountMap.getOrDefault(userAgent.getOs(), 0) + 1);
            allEntries++;
        }
    }

    public HashMap<String, Double> userAgentOSInfo() {
        HashMap<String, Double> OSInfo = new HashMap();
        for (Map.Entry entry : osCountMap.entrySet()) {
            String osName = (String) entry.getKey();
            int count = (int) entry.getValue();
            double proportion = (double) count / allEntries;
            OSInfo.put(osName, proportion);
        }
            return OSInfo;
    }

    public HashSet<String> getPages() {
        return pages;
    }

}
