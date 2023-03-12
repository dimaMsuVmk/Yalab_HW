package HW_1;

import java.util.Scanner;

public class Stars {
    public static void main(String[] args) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            int n = scanner.nextInt();//n - строк
            int m = scanner.nextInt();//m - столбцов
            String template = scanner.next();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    System.out.print(template);
                    if (j != (m - 1)) System.out.print(" ");
                    else System.out.println();
                }
            }
        }
    }
}
