import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Statistics {
    int totalTraffic = 0;
    LocalDateTime minTime;
    LocalDateTime maxTime;
    HashSet<String> hashSet = new HashSet<>();
    HashMap<String, Integer> hashMap = new HashMap<>();
//    UserAgent userAgent;

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
        totalTraffic += logEntry.getContentLength();
        if (logEntry.getStatusCode() == 200) {
            hashSet.add(logEntry.getReferrer()); // Добавляем адрес страницы, если код ответа 200
        }
        if (!logEntry.getUserAgent().equals("-")) {
            UserAgent agent = new UserAgent(logEntry.getUserAgent());
            if (hashMap.containsKey(agent.getOs())) {
                // Если есть, увеличиваем счетчик на 1
                hashMap.put(agent.getOs(), hashMap.get(agent.getOs()) + 1);
            } else {
                // Если нет, добавляем новую запись с начальным значением 1
                hashMap.put(agent.getOs(), 1);
            }
        }
    }

    public double getTrafficRate() {
        Duration duration = Duration.between(minTime, maxTime);
        if (duration.toHours() > 0) return Math.abs(totalTraffic / duration.toHours());
        else return 0;
    }

    public Set<String> getAllPages() {
        return new HashSet<>(hashSet);
    }

    public Map<String, Double> getOSStatistics() {
        HashMap<String, Double> osShareMap = new HashMap<>();

        // Считаем общее количество записей
        int totalCount = hashMap.values().stream().mapToInt(Integer::intValue).sum();

        // Если общее количество равно 0, возвращаем пустую карту
        if (totalCount == 0) {
            return osShareMap;
        }

        // Рассчитываем долю для каждой операционной системы
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            String osName = entry.getKey();
            Integer count = entry.getValue();
            double share = (double) count / totalCount;
            osShareMap.put(osName, share);
        }

        return osShareMap;
    }
}
