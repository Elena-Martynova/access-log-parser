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
    HashSet<String> ExistingPages = new HashSet<>();
    HashMap<String, Integer> hashMapOS = new HashMap<>();
    HashSet<String> notExistingPages = new HashSet<>();
    HashMap<String, Integer> hashMapBrowser = new HashMap<>();
    int browserVisitCount = 0;
    int hoursCount = 0;
    int errRequestsCount = 0;
    int ipCount = 0;
    int userCount = 0;
    HashMap<String, Integer> hashMapUser = new HashMap<>();

    public Statistics() {
    }

    public void addEntry(LogEntry logEntry) {
        UserAgent agent = new UserAgent(logEntry.getUserAgent());
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
            ExistingPages.add(logEntry.getReferrer()); // Добавляем адрес страницы, если код ответа 200
        }
        if (!logEntry.getUserAgent().equals("-")) {
//            agent = new UserAgent(logEntry.getUserAgent());
            if (hashMapOS.containsKey(agent.getOs())) {
                // Если есть, увеличиваем счетчик на 1
                hashMapOS.put(agent.getOs(), hashMapOS.get(agent.getOs()) + 1);
            } else {
                // Если нет, добавляем новую запись с начальным значением 1
                hashMapOS.put(agent.getOs(), 1);
            }
        }
        if (!logEntry.getUserAgent().equals("-")) {
//            UserAgent agent = new UserAgent(logEntry.getUserAgent());
            if (hashMapBrowser.containsKey(agent.getBrowser())) {
                // Если есть, увеличиваем счетчик на 1
                hashMapBrowser.put(agent.getBrowser(), hashMapBrowser.get(agent.getBrowser()) + 1);
            } else {
                // Если нет, добавляем новую запись с начальным значением 1
                hashMapBrowser.put(agent.getBrowser(), 1);
            }
        }
        if (logEntry.getStatusCode() == 404) {
            notExistingPages.add(logEntry.getReferrer()); // Добавляем адрес страницы, если код ответа 404
        }
        if (!logEntry.getUserAgent().contains("bot")) {
            browserVisitCount++;
        }
        hoursCount++;
        ipCount++;
        if (logEntry.getStatusCode()>=400 && logEntry.getStatusCode()<=500) {
            errRequestsCount++;
        }
        if (!logEntry.getIp().equals("-")) {
//            UserAgent agent = new UserAgent(logEntry.getUserAgent());
            if (hashMapUser.containsKey(logEntry.getIp())) {
                // Если есть, увеличиваем счетчик на 1
                hashMapUser.put(logEntry.getIp(), hashMapUser.get(logEntry.getIp()) + 1);
            } else {
                // Если нет, добавляем новую запись с начальным значением 1
                hashMapUser.put(logEntry.getIp(), 1);
            }
        }
    }

    public double getTrafficRate() {
        Duration duration = Duration.between(minTime, maxTime);
        if (duration.toHours() > 0) return Math.abs(totalTraffic / duration.toHours());
        else return 0;
    }

    public Set<String> getExistingPages() {
        return new HashSet<>(ExistingPages);
    }

    public Map<String, Double> getOSStatistics() {
        HashMap<String, Double> osShareMap = new HashMap<>();

        // Считаем общее количество записей
        int totalCount = hashMapOS.values().stream().mapToInt(Integer::intValue).sum();

        // Если общее количество равно 0, возвращаем пустую карту
        if (totalCount == 0) {
            return osShareMap;
        }

        // Рассчитываем долю для каждой операционной системы
        for (Map.Entry<String, Integer> entry : hashMapOS.entrySet()) {
            String osName = entry.getKey();
            Integer count = entry.getValue();
            double share = (double) count / totalCount;
            osShareMap.put(osName, share);
        }

        return osShareMap;
    }

    public Set<String> getNotExistingPages() {
        return new HashSet<>(notExistingPages);
    }

    public Map<String, Double> getBrowserStatistics() {
        HashMap<String, Double> browserShareMap = new HashMap<>();

        // Считаем общее количество записей
        int totalCount = hashMapBrowser.values().stream().mapToInt(Integer::intValue).sum();

        // Если общее количество равно 0, возвращаем пустое значение
        if (totalCount == 0) {
            return browserShareMap;
        }

        // Рассчитываем долю для каждого браузера
        for (Map.Entry<String, Integer> entry : hashMapBrowser.entrySet()) {
            String browserName = entry.getKey();
            Integer count = entry.getValue();
            double share = (double) count / totalCount;
            browserShareMap.put(browserName, share);
        }

        return browserShareMap;
    }

    // Среднее количества посещений сайта за час
    public double getAverageVisitsPerHour() {
        if (hoursCount == 0) {
            return 0;
        }
        return (double) browserVisitCount / hoursCount;
    }

    // Среднее количества ошибочных запросов за час
    public double getAverageErrRequestsPerHour() {
        if (hoursCount == 0) {
            return 0;
        }
        return (double) errRequestsCount / hoursCount;
    }

    // Средняя посещаемость одним пользователем
    public double getAverageVisitsByUser() {
        if (browserVisitCount == 0) {
            return 0;
        }
        return (double) browserVisitCount/hashMapUser.size();
    }
}
