package HW_2.task_ComplexNumbers;

import java.util.Scanner;

public class ComplexNumbersTest {
    public static void main(String[] args) {

        ComplexNumber c1 = new ComplexNumber(-3,-5);
        ComplexNumber c2 = new ComplexNumber(2,1);

        System.out.println("c1 = " + c1);
        System.out.println("c2 = " + c2);

        System.out.println("Complex adding {c1 + c2 = " + c1.add(c2) + '}');

        System.out.println("Complex subtraction {c1 - c2 = " + c1.minus(c2) + '}');

        System.out.println("Complex multiplying {c1 * c2 = " + c1.multiply(c2) + '}');

        System.out.printf("Complex Absolute { |c1| = %.1f} , |c2| = %.1f }",c1.abs(),c2.abs());
    }
}
