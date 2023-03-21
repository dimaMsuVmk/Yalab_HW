package HW_3.task_DatedMap;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class DatedMapImpl implements DatedMap{
    private HashMap<String,String> mapValue = new HashMap();
    private HashMap<String,Date> mapDate = new HashMap();

    @Override
    public void put(String key, String value) {
        mapValue.put(key,value);
        mapDate.put(key,new Date());
    }

    @Override
    public String get(String key) {
        return mapValue.get(key);
    }

    @Override
    public boolean containsKey(String key) {
        return mapValue.containsKey(key);
    }

    @Override
    public void remove(String key) {
        mapValue.remove(key);
        mapDate.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return mapValue.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        return mapDate.get(key);
    }
}
