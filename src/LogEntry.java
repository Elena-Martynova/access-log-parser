import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogEntry {
    public final static String REGEXP = "^(?<ip>\\d{1,3}(?:\\.\\d{1,3}){3})( - - | )\\[(?<date>[^\\]]+)\\] \"(?<method>[A-Z]+) (?<url>[^\\s]+) HTTP\\/(?<httpVersion>[\\d.]+)\" (?<statusCode>\\d{3}) (?<contentLength>\\d+) \"(?<referrer>[^\"]*)\" \"(?<userAgent>[^\"]*)\"$";

    private final String ip;
    private final LocalDateTime data;
    private final HttpMethod method;
    private final String url;
    private final double httpVersion;
    private final int statusCode;
    private final int contentLength;
    private final String referrer;
    private final String userAgent;

    public LogEntry(String line) {
        ip = getValueOfString(line,"$1");

       // Создаем форматтер для парсинга строки даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(getValueOfString(line,"$3"), formatter);
        this.data = offsetDateTime.toLocalDateTime();
        this.method = HttpMethod.valueOf(getValueOfString(line,"$4"));
        this.url = getValueOfString(line,"$5");
        this.httpVersion = Double.parseDouble(getValueOfString(line,"$6"));
        this.statusCode = Integer.parseInt(getValueOfString(line,"$7"));
        this.contentLength = Integer.parseInt(getValueOfString(line,"$8"));
        this.referrer = getValueOfString(line,"$9");
        this.userAgent = getValueOfString(line,"$10");
    }

    static String getValueOfString(String line, String param) {
        return line.replaceAll(REGEXP, param);
    }

    public String getIp() {
        return ip;
    }

    public LocalDateTime getData() {
        return data;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public double getHttpVersion() {
        return httpVersion;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getReferrer() {
        return referrer;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
