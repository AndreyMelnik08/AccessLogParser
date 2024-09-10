package ru.stepup.access.log.parser;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class Statistics {
    private int totalTraffic;
    private int entryCount;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> pages = new HashSet();
    private HashSet<String> nonExistingPages = new HashSet();
    private HashMap<String, Integer> osCountMap = new HashMap();
    private HashMap<String, Integer> browserCountMap = new HashMap();
    private List<ZonedDateTime> timestamps = new ArrayList<>();
    private List<ZonedDateTime> numberOfErroneousRequests = new ArrayList<>();
    private Set<String> ipAdressesNonBot = new HashSet<>();
    private Set<String> domainSet = new HashSet<>();
    private int allEntriesOS = 0;
    private int allEntriesBrowser = 0;

    public Set<String> getDomainSet() {
        return domainSet;
    }

    public void addEntry(LogEntry entry) throws MalformedURLException {
        totalTraffic += entry.getDataSize();
        pagesSet(entry);
        userAgentOSMap(entry.getUserAgent());
        userAgentBrowserMap(entry.getUserAgent());
        LocalDateTime entryTime = entry.getTimestamp();
        averageSiteVisits(entry);
        averageErroneousRequests(entry);
        addRefererDomains(entry);
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

    public void pagesSet(LogEntry entry) {
        if (entry.getResponseCode().getCode() == 404) {
            nonExistingPages.add(entry.getReferer());
        }
        if (entry.getResponseCode().getCode() == 200) {
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

    public void averageSiteVisits(LogEntry logEntry) {
        if (!logEntry.getUserAgent().isBot()) {
            ZonedDateTime zonedDateTime = logEntry.getTimestamp().atZone(ZoneId.systemDefault());
            timestamps.add(zonedDateTime);
            ipAdressesNonBot.add(logEntry.getIpAddress());
        }
    }

    public void averageErroneousRequests(LogEntry logEntry) {
        if (logEntry.getResponseCode().isErroneousRequest()) {
            ZonedDateTime zonedDateTime = logEntry.getTimestamp().atZone(ZoneId.systemDefault());
            numberOfErroneousRequests.add(zonedDateTime);
        }
    }

    public Long calculateAverageRequestsPerHour() {
        Map<ZonedDateTime, Long> requestsPerHour = timestamps.stream().collect(Collectors.groupingBy
                (timestamps -> timestamps.truncatedTo(ChronoUnit.HOURS), Collectors.counting()));
        long totalHours = requestsPerHour.size();
        long totalRequests = requestsPerHour.values().stream().mapToLong(Long::longValue).sum();
        if (totalHours == 0) {
            return 0L;
        }
        return totalRequests / totalHours;
    }

    public Long calculateAverageErroneousRequestsPerHour() {
        Map<ZonedDateTime, Long> erroneousRequestsPerHour = numberOfErroneousRequests.stream().collect(Collectors.groupingBy
                (numberOfErroneousRequests -> numberOfErroneousRequests.truncatedTo(ChronoUnit.HOURS), Collectors.counting()));
        long totalHours = erroneousRequestsPerHour.size();
        long totalRequests = erroneousRequestsPerHour.values().stream().mapToLong(Long::longValue).sum();
        if (totalHours == 0) {
            return 0L;
        }
        return totalRequests / totalHours;
    }

    public Integer calculatePeakVisitsPerSecond() {
        Map<Integer, Integer> visitsPerSecond = new HashMap<>();
        for (ZonedDateTime timestamp : timestamps) {
            int secondKey = (int) timestamp.toEpochSecond();
            visitsPerSecond.put(secondKey, visitsPerSecond.getOrDefault(secondKey, 0) + 1);
        }
        return visitsPerSecond.values().stream().max(Integer::compareTo).orElse(0);
    }

    public void addRefererDomains(LogEntry logEntry) {
        if (!logEntry.getReferer().equals("-")) {
            String decodedReferer = URLDecoder.decode(logEntry.getReferer(), StandardCharsets.UTF_8);
            try {
                URL url = new URL(decodedReferer);
                domainSet.add(url.getHost());
            } catch (MalformedURLException e) {
                System.out.println("Некорректный URL: " + decodedReferer);
            }

        }
    }

    public int maxVisitsBySingleUser() {
        Map<String, Integer> visitCounts = new HashMap<>();
        for (String ipAddress : ipAdressesNonBot) {
            visitCounts.put(ipAddress, visitCounts.getOrDefault(ipAddress, 0) + 1);
        }
        return visitCounts.values().stream().max(Integer::compareTo).orElse(0);
    }

}
