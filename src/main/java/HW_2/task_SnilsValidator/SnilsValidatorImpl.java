package HW_2.task_SnilsValidator;

public class SnilsValidatorImpl implements SnilsValidator {
    @Override
    public boolean validate(String snils) {
        if (snils.length() != 11) return false;

        int[] arr = new int[11];
        int i = 0;

        for (char ch : snils.toCharArray()) {
            if (!Character.isDigit(ch)) return false;
            arr[i++] = Character.digit(ch, 10);
        }
        int sum = 0;
        i = 9;
        for (int j = 1; j <= 9; j++) {
            sum = sum + j * arr[i - j];
        }
        int control = sum < 100 ? sum : (sum == 100 ? 0 : (sum % 101) == 100 ? 0 : (sum % 101));

        return ("" + control).equals("" + arr[9] + arr[10]);
    }
}
