package com.razor.solrcassandra.content;

import org.omg.CORBA.NameValuePair;

import java.util.*;

/**
 * Created by paul.hemmings on 2/15/16.
 */
public class ContentDocument {

    private String name;
    private List<ContentRow> contentItems;

    public List<ContentRow> rows() {
        if (Objects.isNull(this.contentItems)) {
            this.contentItems = new ArrayList<>();
        }
        return contentItems;
    }

    public ContentRow createRow() {
        ContentRow contentRow = new ContentRow();
        this.rows().add(contentRow);
        return contentRow;
    }

    public ContentDocument addContentRow(String key, Object value) {
        ContentRow map = new ContentRow();
        map.put(key, value);
        this.rows().add(map);
        return this;
    }

    public String getName() {
        return name;
    }

    public ContentDocument setName(String name) {
        this.name = name;
        return this;
    }

    public static class ContentRow extends HashMap<String, Object> {
    }
}
