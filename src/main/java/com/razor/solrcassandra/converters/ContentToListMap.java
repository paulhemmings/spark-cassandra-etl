package com.razor.solrcassandra.converters;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by paul.hemmings on 2/23/16.
 */
public class ContentToListMap {

    public List<Map<String, Object>> convert(String content) {
        Gson gson = new Gson();

        if (content.startsWith("[")) {
            Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
            return gson.fromJson(content, type);
        }

        if (content.startsWith("{")) {
            Type type = new TypeToken<Map<String, Object>>(){}.getType();
            Map<String, Object> single = gson.fromJson(content, type);
            return Collections.singletonList(single);
        }
        return null;
    }

    public List<Map<String, Object>> reduce(List<Map<String, Object>>  fullMap, List<String> columnMapping) {
        if (CollectionUtils.isEmpty(fullMap)) {
            return fullMap;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> mapItem : fullMap) {
            Map<String, Object> map = new HashMap<>();
            for (String mapping : columnMapping) {
                String source = mapping.split(":")[0];
                String target = mapping.split(":")[1];
                map.put(target, this.getSource(mapItem, source));
            }
            list.add(map);
        }
        return list;
    }

    public Object getSource(Map<String, Object> mapItem, String source) {
        if (!source.contains(".")) {
            return mapItem.get(source);
        }
        Object leaf = mapItem;
        String[] parts = source.split("\\.");

        for (String part : parts) {
            if (leaf instanceof LinkedTreeMap) {
                leaf = ((LinkedTreeMap)leaf).get(part);
            }
        }
        return leaf;
    }    

}
