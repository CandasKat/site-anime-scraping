package com.projetanime.readFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReadFiles {
    public static void main(String[] args) throws IOException {
        System.out.println(byBufferedReader("editor.csv").size());
    }
    public static Map<String, String> byBufferedReader (String filepath) throws IOException {
        HashMap<String,String > map = new HashMap<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))){
            while ((line = reader.readLine()) != null){
                String[] keyValuePair = line.split(";", 2);
                if (keyValuePair.length > 1){
                    String key = keyValuePair[0];
                    String value = keyValuePair[1];
                    map.put(key,value);
                }
            }
        }
        return map;
    }

    public <K, V>Set<K> getKeys(Map<K,V> map, V value){
        Set<K> keys = new HashSet<>();
        for (Map.Entry<K,V> entry: map.entrySet()){
            if (entry.getValue().equals(value)){
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
}
