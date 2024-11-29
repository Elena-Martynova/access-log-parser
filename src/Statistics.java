import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


public class Statistics {
    int totalTraffic = 0;
    LocalDateTime minTime;
    LocalDateTime maxTime;
    HashSet<String> existingPages = new HashSet<>();
    HashMap<String, Integer> hashMapOS = new HashMap<>();
    HashSet<String> notExistingPages = new HashSet<>();
    HashMap<String, Integer> hashMapBrowser = new HashMap<>();
    int browserVisitCount = 0;
    int errRequestsCount = 0;
    HashMap<String, Integer> hashMapUser = new HashMap<>();
    HashMap<Long, Integer> hashMapVisitsPerSecond = new HashMap<>();
    LocalDateTime currentTime;
    int maxUserPerSecond = 0;
    int maxVisits = 0;
    HashSet<String> domainSet = new HashSet<>();
    HashMap<String,Integer> hashMapUserVisitCount = new HashMap<>();

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
        if (!agent.isBot()) browserVisitCount++;
        if (logEntry.getStatusCode() >= 400 && logEntry.getStatusCode() < 600) errRequestsCount++;
        // Добавляем адрес страницы, если код ответа 200
        if (logEntry.getStatusCode() == 200) existingPages.add(logEntry.getReferrer());

        if (agent.getOs() != null) {
            // Если есть, увеличиваем счетчик на 1
            if (hashMapOS.containsKey(agent.getOs())) hashMapOS.put(agent.getOs(), hashMapOS.get(agent.getOs()) + 1);
                // Если нет, добавляем новую запись с начальным значением 1
            else hashMapOS.put(agent.getOs(), 1);
        }

        if (agent.getBrowser() != null) {
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

        if (logEntry.getIp() != null) {
            // Если есть, увеличиваем счетчик на 1
            if (hashMapUser.containsKey(logEntry.getIp()))
                hashMapUser.put(logEntry.getIp(), hashMapUser.get(logEntry.getIp()) + 1);
                // Если нет, добавляем новую запись с начальным значением 1
            else hashMapUser.put(logEntry.getIp(), 1);
        }

        if (currentTime == null) currentTime = logEntry.getData();

        if (currentTime != null) {
            // Если секунда та же, увеличиваем количество юзеров на 1
            if (currentTime.equals(logEntry.getData())) {
                int currentUserCount = hashMapVisitsPerSecond.getOrDefault(currentTime.toEpochSecond(ZoneOffset.UTC), 0);
                hashMapVisitsPerSecond.put(currentTime.toEpochSecond(ZoneOffset.UTC), currentUserCount + 1);
            }
            // Если нет, добавляем новую запись с начальным значением 1
            else hashMapVisitsPerSecond.put(logEntry.getData().toEpochSecond(ZoneOffset.UTC), 1);
        }

        List<String> referrers = List.of(logEntry.getReferrer());
        for (String referrer : referrers) {
            if (referrer != null && !referrer.isEmpty()) {
                try {
                    URI uri = new URI(logEntry.getReferrer());
                    String domain = uri.getHost();
                    // Если домен не null, добавляем его в Set
                    if (domain != null) {
                        domainSet.add(domain);
                    }
                } catch (URISyntaxException e) {
                    // Обработка некорректного URL
                    System.err.println("Некорректный URL: " + referrer);
                }
            }
        }

        List<String> users = List.of(logEntry.getIp());
        for (String user : users) {
            if (user != null && !user.isEmpty()&& !agent.isBot()) {
                hashMapUserVisitCount.put(logEntry.getIp(), hashMapUserVisitCount.getOrDefault(logEntry.getIp(), 0) + 1);
            }
        }
    }

    // Расчет трафиика в час
    public double getTrafficRate() {
        Duration duration = Duration.between(minTime, maxTime);
        if (duration.toHours() > 0) return Math.abs(totalTraffic / duration.toHours());
        else return 0;
    }

    // Список существующих страниц
    public Set<String> getExistingPages() {
        return new HashSet<>(existingPages);
    }

    // Стастистика ОС
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

    // Список несуществующих страниц
    public Set<String> getNotExistingPages() {
        return new HashSet<>(notExistingPages);
    }

    // Статистика браузеров
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
        Duration duration = Duration.between(minTime, maxTime);
        if (duration.toHours() > 0) return Math.abs(browserVisitCount / duration.toHours());
        else return 0;
    }

    // Среднее количества ошибочных запросов за час
    public double getAverageErrRequestsPerHour() {
        Duration duration = Duration.between(minTime, maxTime);
        if (duration.toHours() > 0) return Math.abs(errRequestsCount / duration.toHours());
        else return 0;
    }

    // Средняя посещаемость одним пользователем
    public double getAverageVisitsByUser() {
        if (browserVisitCount == 0) return 0;
        return (double) browserVisitCount / hashMapUser.size();
    }

    // Максимальная посещаемость сайта в секунду
    public int getVisitsForSecond() {
        hashMapVisitsPerSecond.forEach((K, V) -> {
            if (V > maxUserPerSecond) {
                maxUserPerSecond = V;
            }
        });
        return maxUserPerSecond;
    }

    //Список сайтов, со страниц которых есть ссылки на текущий сайт
    public ArrayList<String> getReferrerDomains(){
        return new ArrayList<>(domainSet);
    }

    //Максимальная посещаемость одним юзером
    public int getMaxVisitsByUser(){
        hashMapUserVisitCount.forEach((K,V) ->{
            if (V > maxVisits) {
                maxVisits = V;
            }
        });
        return maxVisits;
    }

}
