package HW_3.task_FileSort;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Generator {
    public static File generate(String name, int count) throws IOException {

        Random random = new Random();
        File file = new File(name);
        try (PrintWriter pw = new PrintWriter(file)) {
            for (int i = 0; i < count; i++) {
                long rand = random.nextLong();
                pw.println(rand);
            }
            pw.flush();
        }
        return file;
    }
}
