package HW_3.task_FileSort;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        //папка для файлов,временные файлы не использовал,поэтому просто удалим эту папку с мусором после работы программы вручную
        new File("sortFile").mkdir();
        new File("data").mkdir();

        File dataFile = new Generator().generate("data\\data.txt", 10000);
        System.out.println(new Validator(dataFile).isSorted()); // false
        File sortedFile = new Sorter().sortFile(dataFile);
        System.out.println(new Validator(sortedFile).isSorted()); // true
    }
}
