package HW_3.task_FileSort;

import java.io.*;
import java.util.Scanner;

public class Validator {
    private File file;

    public Validator(File file) {
        this.file = file;
    }

    public boolean isSorted() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            long prev = Long.MIN_VALUE;
            while (br.ready()) {
                long current = Long.parseLong(br.readLine() );
                if (current < prev) {
                    return false;
                } else {
                    prev = current;
                }
            }
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
