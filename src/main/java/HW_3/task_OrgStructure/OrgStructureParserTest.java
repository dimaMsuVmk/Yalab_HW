package HW_3.task_OrgStructure;

import java.io.File;
import java.io.IOException;

public class OrgStructureParserTest {
    public static void main(String[] args) throws IOException {
       OrgStructureParser parser = new OrgStructureParserImpl();
       Employee boss = parser.parseStructure(new File("C:\\Users\\Acer\\Desktop\\Собеседование\\Hw1_task1\\src\\main\\resources\\test.csv"));
        System.out.println(boss);
    }
}
