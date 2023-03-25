package HW_1;

import java.util.Scanner;
import java.util.stream.Stream;

//На вход подается число n (0 <= n <= 30), необходимо распечатать n-e число Пелля
public class Pell {
    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Please,write n > 1: ");
            int n = scanner.nextInt();

            System.out.println("First method with cycles:");
            System.out.println("P(n) = "+cyclePell(n));
            System.out.println("___________________");
            System.out.println("Second method with Streams:");
            System.out.println("P(n) = "+streamPell(n));
            System.out.println("___________________");
            System.out.println("Third method with Recursion:");
            System.out.println("P(n) = "+recursivePell(n));
        }
    }
    public static int cyclePell(int n){
        int[] pell = new int[n+2];
        pell[0] = 0;
        pell[1] = 1;
        for (int i = 2; i < pell.length; ++i) {
            pell[i] = 2*pell[i - 1] + pell[i - 2];
        }
        return pell[n];
    }
    public static int streamPell(int n){
        return Stream.iterate(new int[]{0, 1}, arr -> new int[]{arr[1], 2*arr[1]+ arr[0]})
                .limit(n+1)
                .map(arr -> arr[0])
                .reduce((a,b)->b).get();
    }
    public static int recursivePell(int n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else  {
            return 2*recursivePell(n - 1) + recursivePell(n - 2);
        }
    }
}
