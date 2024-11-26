import java.time.Duration;
import java.time.LocalDateTime;

public class Statistics {
    int totalTraffic = 0;
    LocalDateTime minTime;
    LocalDateTime maxTime;

    public Statistics() {
    }

    public void addEntry(LogEntry logEntry) {
        if (minTime == null) minTime = logEntry.getData();
        else if (logEntry.getData().isBefore(minTime)) {
            minTime = logEntry.getData();
        }
        if (maxTime == null) maxTime = logEntry.getData();
        else if (logEntry.getData().isAfter(maxTime)) {
            maxTime = logEntry.getData();
        }
        totalTraffic +=logEntry.getContentLength();
    }

    public double getTrafficRate() {
        Duration duration = Duration.between(minTime, maxTime);
        if (duration.toHours()>0) return Math.abs(totalTraffic/duration.toHours());
        else return 0;
    }
}
