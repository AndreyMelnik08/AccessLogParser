package ru.stepup.access.log.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEntry {

    public enum HttpMethod {GET, POST, PUT, DELETE, CONNECT, PATCH, OPTIONS, HEAD, TRACE}

    private final String ipAddress;
    private final LocalDateTime timestamp;
    private final HttpMethod method;
    private final String requestPath;
    private final int responseCode;
    private final int dataSize;
    private final String referer;
    private final UserAgent userAgent;

    public LogEntry(String logLine) {
        String[] parts = logLine.split(" ");
        this.ipAddress = parts[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");
        this.timestamp = LocalDateTime.parse(parts[1] + " " + parts[2], formatter);
        this.method = HttpMethod.valueOf(parts[3]);
        this.requestPath = parts[4];
        this.responseCode = Integer.parseInt(parts[5]);
        this.dataSize = Integer.parseInt(parts[6]);
        this.referer = parts[7];
        this.userAgent = new UserAgent(parts[8]);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getDataSize() {
        return dataSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }
}
