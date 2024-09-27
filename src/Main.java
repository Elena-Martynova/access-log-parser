import java.util.Scanner;



public class Main {
    public static void main(String[] args) {
        //получение исходных чисел
        System.out.println("Введите первое число: ");
        int firstnumber = new Scanner(System.in).nextInt();
        System.out.println("Введите второе число: ");
        int secondnumber = new Scanner(System.in).nextInt();
        //вычисляем сумму
        int sum = firstnumber+secondnumber;
        //вычисляем разность
        int difference = firstnumber-secondnumber;
        //вычисляем умножение
        int multiplier = firstnumber*secondnumber;
        //вычисляем деление
        double quotient = (double)firstnumber/secondnumber;
        //Выводим результат
        System.out.println("Сумма: " + firstnumber + " + " + secondnumber + " = " + sum);
        System.out.println("Разность: " + firstnumber + " - " + secondnumber + " = " + difference);
        System.out.println("Произведение: " + firstnumber + " * " + secondnumber + " = " + multiplier);
        System.out.println("Частное: " + firstnumber + " / " + secondnumber + " = " + quotient);
    }
}