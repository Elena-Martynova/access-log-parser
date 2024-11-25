import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;


public class Main {
    public final static String REGEXP = "^(?<ip>\\d{1,3}(?:\\.\\d{1,3}){3})( - - | )\\[(?<date>[^\\]]+)\\] \"(?<method>[A-Z]+) (?<url>[^\\s]+) HTTP\\/(?<httpVersion>[\\d.]+)\" (?<statusCode>\\d{3}) (?<contentLength>\\d+) \"(?<referrer>[^\"]*)\" \"(?<userAgent>[^\"]*)\"$";
    public static final String botYandex = "YandexBot";
    public static final String botGoogle = "Googlebot";
    public static final int counterYandex=0;
    public static final int counterGoogle=0;

    public static void main(String[] args) {

        String path = new Scanner(System.in).nextLine();
        File file = new File(path);
//        final Pattern pattern = Pattern.compile("REGEXP");
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
            while ((line = reader.readLine()) != null) {
                int length = line.length();
                lineCounter += 1;
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
                        String fragment = parts[1];
                        fragment = fragment.replaceAll(" ", "");
                        fragment = fragment.substring(0, fragment.indexOf("/"));

                        System.out.println(fragment);
                    }
                }

//                System.out.println("ipAddress: " + getIpAddress(line));
//                System.out.println("NullPoint: " + getNullPoint(line));
//                System.out.println("Date: " + getDate(line));
//                System.out.println("Method and Url: " + getMethod(line) + getUrl(line));
//                System.out.println("StatusCode: "+ geStatusCode(line));
//                System.out.println("ContetntLength: "+getContentLength(line));
//                System.out.println("Referer: "+getReferer(line));
//                System.out.println("UserAgent: "+getUserAgent(line));
//                String userAgent = getUserAgent(line);

            }
            if (maxLineLength > 1024) {
                throw new MaxLineException("Max line length exceeds 1024 symbols.");
            }
            System.out.println("Количество строк в файле: " + lineCounter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static String getValueOfString(String line, String param) {
        return line.replaceAll(REGEXP, param);
    }

//    static String getIpAddress(String line) {
//        String ip = line.replaceAll("REGEXP", "$1");
//        return ip;
//    }
//
//    static String getNullPoint(String line) {
//        String nullPoints = line.replaceAll("REGEXP", "$2");
//        return nullPoints;
//    }
//
//    static String getDate(String line) {
////        Pattern pattern = Pattern.compile("REGEXP");
//        String date = line.replaceAll("REGEXP", "$3");
//        return date;
//    }
//
//    static String getMethod(String line) {
//        String method = line.replaceAll("REGEXP", "$4");
//        return method;
//    }
//
//    static String getUrl(String line) {
//        String url = line.replaceAll("REGEXP", "$5");
//        return url;
//    }
//
//    static String geStatusCode(String line) {
//        String statusCode = line.replaceAll("REGEXP", "$7");
//        return statusCode;
//    }
//
//    static String getContentLength(String line) {
//        String contentLength = line.replaceAll("REGEXP", "$8");
//        return contentLength;
//    }
//
//    static String getReferer(String line) {
//        String referer = line.replaceAll("REGEXP", "$9");
//        return referer;
//    }
//
//    static String getUserAgent(String line) {
//        String userAgent = line.replaceAll("REGEXP", "$10");
//        return userAgent;
//    }


}

class MaxLineException extends Exception {
    public MaxLineException(String message) {
        super(message);
    }
}

