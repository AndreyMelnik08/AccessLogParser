package ru.stepup.access.log.parser;

public class UserAgent {
    private final String os;
    private final String browser;


    public UserAgent(String userAgentString) {
        this.os = extractOS(userAgentString);
        this.browser = extractBrowser(userAgentString);
    }

    private String extractOS(String userAgent) {
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Mac OS")) {
            return "macOS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        }
        return "Unknown OS";
    }

    private String extractBrowser(String userAgent) {
        if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Opera")) {
            return "Opera";
        }
        return "Other";
    }

    public String getOs() {
        return os;
    }

    public String getBrowser() {
        return browser;
    }
}
