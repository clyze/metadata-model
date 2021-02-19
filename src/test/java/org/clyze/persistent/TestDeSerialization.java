package org.clyze.persistent;

import com.google.gson.Gson;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

import org.clyze.persistent.metadata.Configuration;
import org.clyze.persistent.metadata.Printer;
import org.clyze.persistent.metadata.SourceFileReporter;
import org.clyze.persistent.metadata.SourceMetadata;
import org.clyze.persistent.model.*;
import org.junit.jupiter.api.Test;

public class TestDeSerialization {

    @Test
    public void test() {
        Position pos = new Position(0, 1, 2, 3);
        Type type1 = new Type(pos, "sourceFileName.c", "unique-symbolId", "name");
        Type type2 = new Type();
        Map<String, Object> map1 = type1.toMap();
        type2.fromMap(map1);

        assert type1.equals(type2);
        assert itemEquals(type1, type2);

        Configuration configuration = new Configuration(new Printer(true));
        SourceMetadata elements = new SourceMetadata();
        elements.types.add(type1);
        SourceFileReporter reporter = new SourceFileReporter(configuration, elements);
        String outPath = "build/test-metadata.json";
        reporter.createReportFile(outPath);
        reporter.printReportStats();

        try {
            String json = new String(Files.readAllBytes((new File(outPath)).toPath()));
            Gson gson = new Gson();
            Map<String, Object> map = gson.fromJson(json, Map.class);
            List<Map<String, Object>> types = (List<Map<String, Object>>) map.get("Type");
            assert types != null;
            assert mapEquals(map1, types.get(0));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * An expensive way to compare items by comparing their serialized maps.
     * @param item1   the first item
     * @param item2   the second item
     * @return        true if both items are equal (as maps)
     */
    private boolean itemEquals(Item item1, Item item2) {
        Map<String, Object> map1 = item1.toMap();
        Map<String, Object> map2 = item2.toMap();
        return mapEquals(map1, map2);
    }

    private boolean keyMismatchNotEqual(String key, Map<String, Object> map1, Map<String, Object> map2) {
        return map1.containsKey(key) && !map2.containsKey(key) && map1.get(key) != null;
    }

    private boolean mapEquals(Map<String, Object> map1, Map<String, Object> map2) {
        Set<String> keys1 = map1.keySet();
        Set<String> keys2 = map2.keySet();
        Set<String> allKeys = new HashSet<>(keys1);
        allKeys.addAll(keys2);
        for (String key : allKeys)
            if (keyMismatchNotEqual(key, map1, map2) || keyMismatchNotEqual(key, map2, map1)) {
                System.err.println("ERROR: maps have different keys: " + keys1 + " vs. " + keys2);
                return false;
            }
        boolean result = true;
        for (Map.Entry<String, Object> entry1 : map1.entrySet()) {
            String key = entry1.getKey();
            Object obj1 = entry1.getValue();
            Object obj2 = map2.get(key);
            if (obj1 == null && obj2 == null)
                continue;
            else if (obj1 != null && obj2 != null) {
                if (obj1 instanceof Map && obj2 instanceof Map) {
                    result &= mapEquals((Map<String, Object>) obj1, (Map<String, Object>) obj2);
                } else if (obj1 instanceof List && obj2 instanceof List) {
                    if (!listEquals((List<Object>)obj1, (List<Object>)obj2))
                        return false;
                } else if (!jsonValueEquals(obj1, obj2)) {
                    System.err.println("ERROR: maps are different at key '" + key +"': " + obj1 + ", " + obj2);
                    return false;
                }
            } else {
                System.err.println("ERROR: one of the maps is null: " + map1 + ", " + map2);
                return false;
            }
        }
        return result;
    }

    private boolean listEquals(List<Object> list1, List<Object> list2) {
        if (list1 == null && list2 == null)
            return true;
        if (list1 != null && list2 != null) {
            if (list1.size() != list2.size()) {
                System.err.println("ERROR: lists have different size: " + list1 + ", " + list2);
                return false;
            } else {
                for (int i = 0; i < list1.size(); i++) {
                    Object obj1 = list1.get(i);
                    Object obj2 = list2.get(i);
                    if (obj1 instanceof Map && obj2 instanceof Map) {
                        if (!mapEquals((Map<String, Object>) obj1, (Map<String, Object>) obj2))
                            return false;
                    } else if (obj1 instanceof List && obj2 instanceof List) {
                        if (!listEquals((List<Object>)obj1, (List<Object>)obj2))
                            return false;
                    } else if (!jsonValueEquals(obj1, obj2)) {
                        System.err.println("ERROR: lists are different at index " + i +": " + obj1 + ", " + obj2);
                        return false;
                    }
                }
                return true;
            }
        } else {
            System.err.println("ERROR: one of the lists is null: " + list1 + ", " + list2);
            return false;
        }
    }

    /**
     * Check two JSON values for equality. If the two values are numbers,
     * compare via conversion to double to address any implicit conversions.
     * Collections are compared by first converting them to sorted lists.
     * @param obj1    the first value
     * @param obj2    the second value
     * @return        true if the values are equal
     */
    private boolean jsonValueEquals(Object obj1, Object obj2) {
//        System.out.println(obj1.getClass().getCanonicalName());
//        System.out.println(obj2.getClass().getCanonicalName());
        if (obj1.equals(obj2))
            return true;
        if (obj1 instanceof Number && obj2 instanceof Number)
            return getDouble((Number)obj1) == getDouble((Number)obj2);
        if (obj1 instanceof Collection && obj2 instanceof Collection) {
            TreeSet<Object> set1 = new TreeSet<>((Collection<?>) obj1);
            TreeSet<Object> set2 = new TreeSet<>((Collection<?>) obj2);
            return listEquals(Arrays.asList(set1.toArray()), Arrays.asList(set2.toArray()));
        }
        return false;
    }

    /**
     * Convert numbers to double for maximum precision.
     * @param num   the number to convert
     * @return      the equivalent double
     */
    private double getDouble(Number num) {
        String str = num.toString();
        return Double.parseDouble(str);
    }
}