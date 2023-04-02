package io.ylab.intensive.lesson04.persistentmap;

import java.sql.SQLException;
import javax.sql.DataSource;

import io.ylab.intensive.lesson04.DbUtil;
public class PersistenceMapTest {
  public static void main(String[] args) throws SQLException {
    DataSource dataSource = initDb();
    PersistentMap persistentMap_1 = new PersistentMapImpl(dataSource);
    persistentMap_1.init("persistentMap_1");
    // Написать код демонстрации работы
    persistentMap_1.put("Dima","MSU");
    persistentMap_1.put("Vika","MFUA");
    persistentMap_1.put("Igor","MEPHI");
    persistentMap_1.put("Oleg","MSU");
    persistentMap_1.put("Misha","Baymanka");
    //создатим вторую мапу и убедимся,что манипуляции с первой мапой не меняют данные второй
    PersistentMap map_2 = new PersistentMapImpl(dataSource);
    map_2.init("map_2");
    map_2.put("Dima","MSU");
    map_2.put("Vika","MFUA");
    map_2.put("Igor","MEPHI");
    map_2.put("Oleg","MSU");
    map_2.put("Misha","Baymanka");

    //test containsKey(String key)
    System.out.println("persistentMap_1.containsKey(\"Dima\") : " + persistentMap_1.containsKey("Dima"));
    System.out.println("persistentMap_1.containsKey(\"NotThisKey\") : " + persistentMap_1.containsKey("NotThisKey"));
    //test getKeys()
    System.out.println("persistentMap_1.getKeys(): " + persistentMap_1.getKeys());
    //test remove(String key)
    persistentMap_1.remove("Vika");
    System.out.println("persistentMap_1.remove(\"Vika\"): " + persistentMap_1.getKeys());
    //test get(String key)
    System.out.println("persistentMap_1.get(\"Igor\") must be \"MEPHI\": " + persistentMap_1.get("Igor"));
    //test put(key,value) - обновляем! значение по ключу,ключ уже был в БД
    System.out.println("check put(\"Igor\",\"MIREA\") the same key");
    persistentMap_1.put("Igor","MIREA");
    System.out.println("persistentMap_1.get(\"Igor\") must be \"MIREA\": " + persistentMap_1.get("Igor"));
    //test clear()
    persistentMap_1.clear();
    System.out.println("list keys After persistentMap_1.clear(): " + persistentMap_1.getKeys());
    //map_2.getKeys()
    System.out.println("map_2.getKeys(): " + map_2.getKeys());

    System.out.println("\nсоздадим мапу с СУЩЕСТВУЮЩИМ ИМЕНЕМ \"map_2\",должно быть исключение");
    try {
      PersistentMap pm = new PersistentMapImpl(dataSource);
      pm.init("map_2");
    }catch (Exception e){
      System.out.println("ERROR: " + e.getMessage());
    }

    System.out.println("\nобъект \"PersistentMap\" БЕЗ имени не должен поддерживать никакие операции,Попробуем создать мапу без имени");
    PersistentMap mapWithoutName = new PersistentMapImpl(dataSource);
    try{
      mapWithoutName.put("Igor","MSU");
    }catch (Exception e){
      System.out.println("ERROR: " + e.getMessage());
    }

  }
  
  public static DataSource initDb() throws SQLException {
    String createMapTable = "" 
                                + "drop table if exists persistent_map; " 
                                + "CREATE TABLE if not exists persistent_map (\n"
                                + "   map_name varchar,\n"
                                + "   KEY varchar,\n"
                                + "   value varchar\n"
                                + ");";
    DataSource dataSource = DbUtil.buildDataSource();
    DbUtil.applyDdl(createMapTable, dataSource);
    return dataSource;
  }
}
