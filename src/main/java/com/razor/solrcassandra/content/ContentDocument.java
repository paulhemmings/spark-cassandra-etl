package com.razor.solrcassandra.content;

import org.omg.CORBA.NameValuePair;

import java.util.*;

/**
 * Created by paul.hemmings on 2/15/16.
 */
public class ContentDocument extends ArrayList<Map<String, Object>> {

    public Map<String, Object> createRow() {
        Map<String, Object> contentRow = new HashMap<>();
        this.add(contentRow);
        return contentRow;
    }

    public ContentDocument addContentRow(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        this.add(map);
        return this;
    }
}
