package ru.stepup.access.log.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final Pattern LOG_PATTERN = Pattern.compile("(\\S+) - - \\[(.+?)\\] \"(\\S+) (.+?) HTTP/\\S+\" (\\d{3}) (\\d+) \"([^\"]*)\" \"([^\"]*?)\"");

    public LogEntry(String logLine) {
        Matcher matcher = LOG_PATTERN.matcher(logLine);
        if (matcher.find()) {
            ipAddress = matcher.group(1);
            timestamp = LocalDateTime.parse(matcher.group(2), DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z").withLocale(Locale.ENGLISH));
            method = HttpMethod.valueOf(matcher.group(3));
            requestPath = matcher.group(4);
            responseCode = Integer.parseInt(matcher.group(5));
            dataSize = Integer.parseInt(matcher.group(6));
            referer = matcher.group(7);
            userAgent = new UserAgent(matcher.group(8));
            return;
        }
        throw new IllegalArgumentException("Invalid log entry format");
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

    @Override
    public String toString() {
        return "LogEntry{" +
                "ipAddress='" + ipAddress + '\'' +
                ", timestamp=" + timestamp +
                ", method=" + method +
                ", requestPath='" + requestPath + '\'' +
                ", responseCode=" + responseCode +
                ", dataSize=" + dataSize +
                ", referer='" + referer + '\'' +
                ", userAgent=" + userAgent +
                '}';
    }
}

