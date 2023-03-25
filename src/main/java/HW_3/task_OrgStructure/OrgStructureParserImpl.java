package HW_3.task_OrgStructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class OrgStructureParserImpl implements OrgStructureParser {
    @Override
    public Employee parseStructure(File csvFile) throws IOException {
        Employee boss = null;
        try (FileInputStream fileInputStream = new FileInputStream(csvFile);
             Scanner scanner = new Scanner(fileInputStream)) {
            scanner.nextLine();
            Map<Long, Employee> map = new HashMap<>();
            while (scanner.hasNextLine()) {
                String stringFromFile = scanner.nextLine();
                Employee employee = new Employee();
                //id;boss_id;name;position
                String[] arr = stringFromFile.split(";");

                employee.setId(Long.parseLong(arr[0]));
                if (!arr[1].equals("")) employee.setBossId(Long.parseLong(arr[1]));
                else boss = employee;
                employee.setName(arr[2]);
                employee.setPosition(arr[3]);
                map.put(employee.getId(), employee);
            }
            for (Employee e : map.values()) {
                e.setBoss(map.get(e.getBossId()));
                Long bossId = e.getBossId();
                if (bossId != null) {
                    map.get(bossId).getSubordinate().add(e);
                }
            }
        }
        return boss;
    }
}
