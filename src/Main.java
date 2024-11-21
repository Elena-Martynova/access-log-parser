import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
            int length = 0;
            int maxLineLength = 0;
            line = reader.readLine();
            int minLineLength = line.length();
            while ((line = reader.readLine()) != null) {
                length = line.length();
                lineCounter += 1;
                if (length > maxLineLength) {
                    maxLineLength = length;
                }
                if (length < minLineLength) {
                    minLineLength = length;
                }
            }
            if (maxLineLength > 1024) {
                throw new MaxLineException("Max line length exceeds 1024 symbols.");
            }
            System.out.println("Количество строк в файле: " + lineCounter);
            System.out.println("Самая длинная строка: " + maxLineLength);
            System.out.println("Самая короткая строка: " + minLineLength);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class MaxLineException extends Exception {
    public MaxLineException(String message) {
        super(message);
    }
}
