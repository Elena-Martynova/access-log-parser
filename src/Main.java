import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int n = 0;
        while (true) {
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (isDirectory==true) {
                System.out.println("Путь указан неверно");
            }
            if (fileExists == true /*&& isDirectory == true*/) {
                System.out.println("Путь указан верно");
                n = n+1;
                System.out.println("Это файл номер " + n);
            }
//            else {
//                System.out.println("Путь указан неверно");
//            }

        }
    }
}