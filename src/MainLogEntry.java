import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class MainLogEntry {
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
            System.out.println(statistics.getTrafficRate());
            System.out.println(statistics.getExistingPages());
            System.out.println(statistics.getNotExistingPages());
            System.out.println(statistics.getOSStatistics());
            System.out.println(statistics.getBrowserStatistics());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
