package HW_2.task_Sequences;

import java.util.Scanner;

public class SequencesTest {
    public static void main(String[] args) {
        int n;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Vvedite chislo \"n\"");
            n = scanner.nextInt();
        }
        if (n <= 0) throw new IllegalArgumentException("n must be great then 0");
        SequencesImpl sequences = new SequencesImpl();
        System.out.println("Lets go testing methods ...");

        System.out.println("A: 2, 4, 6, 8, 10...");
        sequences.a(n);
        System.out.println();

        System.out.println("B: 1, 3, 5, 7, 9...");
        sequences.b(n);
        System.out.println();

        System.out.println("C: 1, 4, 9, 16, 25...");
        sequences.c(n);
        System.out.println();

        System.out.println("D: 1, 8, 27, 64, 125...");
        sequences.d(n);
        System.out.println();

        System.out.println("E: 1, -1, 1, -1, 1, -1...");
        sequences.e(n);
        System.out.println();

        System.out.println("F: 1, -2, 3, -4, 5, -6...");
        sequences.f(n);
        System.out.println();

        System.out.println("G: 1, -4, 9, -16, 25....");
        sequences.g(n);
        System.out.println();

        System.out.println("H: 1, 0, 2, 0, 3, 0, 4....");
        sequences.h(n);
        System.out.println();

        System.out.println("I: 1, 2, 6, 24, 120, 720...");
        sequences.i(n);
        System.out.println();

        System.out.println("J: 1, 1, 2, 3, 5, 8, 13, 21…");
        sequences.j(n);
        System.out.println();

    }
}
