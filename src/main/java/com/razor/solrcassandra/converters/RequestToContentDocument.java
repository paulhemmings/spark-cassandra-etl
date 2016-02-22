package com.razor.solrcassandra.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.razor.solrcassandra.content.ContentDocument;
import spark.Request;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by paul.hemmings on 2/22/16.
 */

public class RequestToContentDocument {

    public ContentDocument convert(Request request) {

        ContentDocument contentDocument;
        Gson gson = new Gson();
        String requestBody = request.body();

        if (requestBody.startsWith("[")) {
            Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
            List<Map<String, Object>> collection = gson.fromJson(requestBody, type);
            contentDocument = this.convert(collection);
        } else {
            contentDocument = gson.fromJson(requestBody, ContentDocument.class);
        }

        return contentDocument;

    }

    public ContentDocument convert(List<Map<String, Object>> collection) {
        final ContentDocument contentDocument = new ContentDocument();
        collection.forEach(item -> {
            ContentDocument.ContentRow row = contentDocument.createRow();
            item.keySet().stream().forEach(key -> row.put(key, item.get(key)));
        });
        return contentDocument;
    }
}
