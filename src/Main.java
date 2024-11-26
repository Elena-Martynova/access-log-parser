import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {
    public final static String REGEXP = "^(?<ip>\\d{1,3}(?:\\.\\d{1,3}){3})( - - | )\\[(?<date>[^\\]]+)\\] \"(?<method>[A-Z]+) (?<url>[^\\s]+) HTTP\\/(?<httpVersion>[\\d.]+)\" (?<statusCode>\\d{3}) (?<contentLength>\\d+) \"(?<referrer>[^\"]*)\" \"(?<userAgent>[^\"]*)\"$";
    public static final String botYandex = "YandexBot";
    public static final String botGoogle = "Googlebot";
    public static int counterYandex = 0;
    public static int counterGoogle = 0;

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
            int lineCounter = 0;
            int maxLineLength = 0;
            double partOfYandex = 0;
            double partOfGoogle = 0;
            while ((line = reader.readLine()) != null) {

                int length = line.length();
                lineCounter++;
                if (length > maxLineLength) {
                    maxLineLength = length;
                }
//                System.out.println(getValueOfString(line, "$10"));
                int startIndex = line.indexOf("(compatible");
                int endIndex = line.lastIndexOf(")");
                if (line.contains(botYandex) || line.contains(botGoogle)) {
                    line = line.substring(startIndex + 1, endIndex);
                    String[] parts = line.split(";");
                    if (parts.length >= 2) {
                        String fragment = parts[1].replaceAll(" ", "");
                        String fragmentBot = fragment.substring(0, fragment.indexOf("/"));
                        if (fragmentBot.equals(botGoogle)) counterGoogle++;
                        if (fragmentBot.equals(botYandex)) counterYandex++;
                        partOfYandex = (double) counterYandex / lineCounter * 100;
                        partOfGoogle = (double) counterGoogle / lineCounter * 100;
                    }
                }
            }
            if (maxLineLength > 1024) {
                throw new MaxLineException("Max line length exceeds 1024 symbols.");
            }
            System.out.println("Количество запросов YandexBot: " + counterYandex);
            System.out.println("Количество запросов Googlebot: " + counterGoogle);
            System.out.println("Количество строк в файле: " + lineCounter);
            System.out.println("Доля запросов YandexBot: " + String.format("%.2f",partOfYandex));
            System.out.println("Доля запросов Googlebot: " + String.format("%.2f",partOfGoogle));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static String getValueOfString(String line, String param) {
        return line.replaceAll(REGEXP, param);
    }
}

class MaxLineException extends Exception {
    public MaxLineException(String message) {
        super(message);
    }
}


