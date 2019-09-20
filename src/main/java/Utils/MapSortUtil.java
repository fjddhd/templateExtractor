package Utils;

import java.util.*;

public class MapSortUtil {
    // 按照键排序
    public static void sortByKey(Map map) {
        Object[] objects = map.keySet().toArray();
        Arrays.sort(objects);
        for (int i = 0; i < objects.length; i++) {
            System.out.println("键：" + objects[i] + " 值：" + map.get(objects[i]));
        }
    }

    // 按照值排序
    public static List<String> sortByValue(Map map) {
        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        List<String> res=new ArrayList<String>();

        for (Map.Entry<String, Integer> mapping : list) {
            System.out.println("键：" + mapping.getKey() + " 值：" + mapping.getValue());

        }
        for (Map.Entry<String, Integer> mapping : list){
            res.add(mapping.getKey());
        }
        return res;
    }


}
