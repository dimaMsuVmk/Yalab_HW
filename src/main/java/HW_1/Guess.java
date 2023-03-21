package HW_1;

import java.util.Random;
import java.util.Scanner;

public class Guess {
    public static void main(String[] args) throws Exception {
        int number = new Random().nextInt(100); // здесь загадывается число от 1 до 99
        int maxAttempts = 10; // здесь задается количество попыток
        System.out.println("Я загадал число. У тебя " + maxAttempts + " попыток угадать.");
        int current = -1;
        try (Scanner scanner = new Scanner(System.in)){
            for (int i = 1; i <= maxAttempts; i++) {
                current = scanner.nextInt();
                if(number == current) {
                    System.out.println("Ты угадал с "+i+" попытки");
                    break;
                }else if(current > number){
                    System.out.println("Мое число меньше! У тебя осталось "+(maxAttempts-i)+" попыток");
                }else {
                    System.out.println("Мое число больше! У тебя осталось "+(maxAttempts-i)+" попыток");
                }
                if(i == maxAttempts){
                    System.out.println("Ты нe угадал");
                }
            }
        }
    }
}
