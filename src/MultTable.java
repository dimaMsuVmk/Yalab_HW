
//На вход ничего не подается, необходимо распечатать таблицу умножения чисел от 1 до 9 (включая)
public class MultTable {
    public static void main(String[] args) throws Exception {
        second();
        System.out.println("-----------");
        first();
    }

    private static void first(){
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                System.out.println(i+" x "+j+" = "+i*j);
                if(j == 9) System.out.println();
            }
        }
    }
    //optional
    private static void second(){
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                System.out.printf(j+" x "+i+" = %-2d   ",i*j);
            }
            System.out.println();
        }
    }
}
