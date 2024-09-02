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
    private HashSet<String> nonExistingPages = new HashSet();
    private HashMap<String, Integer> osCountMap = new HashMap();
    private HashMap<String, Integer> browserCountMap = new HashMap();
    private int allEntriesOS = 0;
    private int allEntriesBrowser = 0;

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getDataSize();
        pagesSet(entry);
        userAgentOSMap(entry.getUserAgent());
        userAgentBrowserMap(entry.getUserAgent());
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

    public void userAgentOSMap(UserAgent userAgent) {
        if (userAgent.getOs().equals("Windows") || userAgent.getOs().equals("macOS") || userAgent.getOs().equals("Linux")) {
            osCountMap.put(userAgent.getOs(), osCountMap.getOrDefault(userAgent.getOs(), 0) + 1);
            allEntriesOS++;
        }
    }

    public void userAgentBrowserMap(UserAgent userAgent) {
        if (userAgent.getBrowser().equals("Edge") || userAgent.getBrowser().equals("Firefox") || userAgent.getBrowser().equals("Chrome") || userAgent.getBrowser().equals("Opera")) {
            browserCountMap.put(userAgent.getBrowser(), browserCountMap.getOrDefault(userAgent.getBrowser(), 0) + 1);
            allEntriesBrowser++;
        }
    }

    public void pagesSet (LogEntry entry) {
        if (entry.getResponseCode() == 404) {
            nonExistingPages.add(entry.getReferer());
        }
        if (entry.getResponseCode() == 200) {
            pages.add(entry.getReferer());
        }
    }

    public HashMap<String, Double> userAgentOSInfo() {
        HashMap<String, Double> OSInfo = new HashMap();
        for (Map.Entry entry : osCountMap.entrySet()) {
            String osName = (String) entry.getKey();
            int count = (int) entry.getValue();
            double proportion = (double) count / allEntriesOS;
            OSInfo.put(osName, proportion);
        }
            return OSInfo;
    }

    public HashMap<String, Double> userAgentBrowserInfo() {
        HashMap<String, Double> browserInfo = new HashMap();
        for (Map.Entry entry : browserCountMap.entrySet()) {
            String browserName = (String) entry.getKey();
            int count = (int) entry.getValue();
            double proportion = (double) count / allEntriesBrowser;
            browserInfo.put(browserName, proportion);
        }
        return browserInfo;
    }



    public HashSet<String> getPages() {
        return pages;
    }

    public HashSet<String> getNonExistingPages() {
        return nonExistingPages;
    }
}
