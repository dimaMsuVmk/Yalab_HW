package HW_2.task_Sequences;

public class SequencesImpl implements Sequences {

    @Override
    public void a(int n) {
        //2, 4, 6, 8, 10...
        System.out.print("A: ");
        for (int i = 1; i <= n; i++) {
            System.out.print(2 * i + "  ");
        }
        System.out.println();
    }

    @Override
    public void b(int n) {
        //1, 3, 5, 7, 9...
        System.out.print("B: ");
        for (int i = 1; i <= n; i++) {
            System.out.print(1 + (i - 1) * 2 + "  ");
        }
        System.out.println();
    }

    @Override
    public void c(int n) {
        //1, 4, 9, 16, 25...
        System.out.print("C: ");
        for (int i = 1; i <= n; i++) {
            System.out.print(i * i + "  ");
        }
        System.out.println();
    }

    @Override
    public void d(int n) {
        //1, 8, 27, 64, 125...
        System.out.print("D: ");
        for (int i = 1; i <= n; i++) {
            System.out.print(i * i * i + "  ");
        }
        System.out.println();
    }

    @Override
    public void e(int n) {
        // 1, -1, 1, -1, 1, -1...
        System.out.print("E: ");
        int a = -1;
        for (int i = 1; i <= n; i++) {
            a = a * (-1);
            System.out.print(a + "  ");
        }
        System.out.println();
    }

    @Override
    public void f(int n) {
        //1, -2, 3, -4, 5, -6...
        System.out.print("F: ");
        int a = -1;
        for (int i = 1; i <= n; i++) {
            a = a * (-1);
            System.out.print(i * a + "  ");
        }
        System.out.println();
    }

    @Override
    public void g(int n) {
        //1, -4, 9, -16, 25....
        System.out.print("G: ");
        int a = -1;
        for (int i = 1; i <= n; i++) {
            a = a * (-1);
            System.out.print(a * i * i + "  ");
        }
        System.out.println();
    }

    @Override
    public void h(int n) {
        //1, 0, 2, 0, 3, 0, 4....
        System.out.print("H: ");
        int a = 1;
        for (int i = 1; i <= n; i++) {
            if (i % 2 == 0) System.out.print(0 + "  ");
            else {
                System.out.print(a + "  ");
                a++;
            }
        }
        System.out.println();
    }

    @Override
    public void i(int n) {
        //1, 2, 6, 24, 120, 720...
        System.out.print("I: ");
        int a = 1;
        for (int i = 1; i <= n; i++) {
            a = a * i;
            System.out.print(a + "  ");
        }
        System.out.println();
    }

    @Override
    public void j(int n) {
        //1, 1, 2, 3, 5, 8, 13, 21…
        System.out.print("J: ");
        int a = 0, b = 1, buf = 0;
        for (int i = 1; i <= n; i++) {
            System.out.print(b + "  ");
            buf = b;
            b = b + a;
            a = buf;
        }
        System.out.println();
    }
}
