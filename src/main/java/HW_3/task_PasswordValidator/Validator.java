package HW_3.task_PasswordValidator;

import HW_3.task_PasswordValidator.exception.WrongLoginException;
import HW_3.task_PasswordValidator.exception.WrongPasswordException;

public class Validator {
    public static void main(String[] args) {
        boolean b = Validator.valid("Dima_Vmk","Polina_35","Polina_35");
        System.out.println(b);
    }
    public static boolean valid(String login, String password, String confirmPassword) {
        try {
            if (!login.matches("[a-zA-Z0-9_]*")) throw new WrongLoginException("Логин содержит недопустимые символы");
            if (login.length() >= 20) throw new WrongLoginException("Логин слишком длинный");
            if (!password.matches("[a-zA-Z0-9_]*")) throw new WrongPasswordException("Пароль содержит недопустимые символы");
            if (password.length() >= 20) throw new WrongPasswordException("Пароль слишком длинный");
            if (!password.equals(confirmPassword)) throw new WrongPasswordException("Пароль и подтверждение не совпадают");
        }catch (WrongPasswordException | WrongLoginException e){
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
}
