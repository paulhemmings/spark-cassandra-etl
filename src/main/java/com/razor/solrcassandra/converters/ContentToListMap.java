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

}
