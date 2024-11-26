public class UserAgent {
    private final String browser;
    private final String os;

    public UserAgent(String userAgent) {
        browser = extractBrowser(userAgent);
        os = extractOsType(userAgent);
    }

    private String extractOsType(String userAgent) {
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Mac OS")) {
            return "macOS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        }
        return "Unknown";
    }

    private String extractBrowser(String userAgent) {
        if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("Opera")) {
            return "Opera";
        }
        return "Other";
    }

    public boolean isBot(String userAgent) {
        return userAgent.toLowerCase().contains("bot");
    }


}
