import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class MainStatistics {
    public static void main(String[] args) {
        System.out.println("Введите путь к файлу: ");
        String path = new Scanner(System.in).nextLine();
        File file = new File(path);
        boolean fileExists = file.exists();
        boolean isDirectory = file.isDirectory();
        if (isDirectory) {
            System.out.println("Путь указан неверно.");
        }
        if (fileExists) {
            System.out.println("Путь указан верно.");
        } else {
            System.out.println("Путь указан неверно.");
        }
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            LogEntry logEntry;

            Statistics statistics = new Statistics();
            while ((line = reader.readLine()) != null) {
                logEntry = new LogEntry(line);
                statistics.addEntry(logEntry);
            }
            System.out.println("TrafficRate: " + statistics.getTrafficRate());
            System.out.println("ExistingPages: " + statistics.getExistingPages());
            System.out.println("NotExistingPages: " + statistics.getNotExistingPages());
            System.out.println("OSStatistics: " + statistics.getOSStatistics());
            System.out.println("BrowserStatistics: " + statistics.getBrowserStatistics());
            System.out.println("AverageVisitsPerHour " + statistics.getAverageVisitsPerHour());
            System.out.println("AverageErrRequestsPerHour: " + statistics.getAverageErrRequestsPerHour());
            System.out.println("AverageVisitsByUser: " + statistics.getAverageVisitsByUser());
            System.out.println("MaxVisitsPerSecond: " + statistics.getVisitsForSecond());
            System.out.println("ListOfReferrerDomains: " + statistics.getReferrerDomains());
            System.out.println("MaxVisitsByUser: " + statistics.getMaxVisitsByUser());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
