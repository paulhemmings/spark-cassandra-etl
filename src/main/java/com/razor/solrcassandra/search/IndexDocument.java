package com.razor.solrcassandra.search;

import com.razor.solrcassandra.content.ContentLoadRequest;

import java.util.List;

/**
 * Created by paul.hemmings on 2/12/16.
 */

public class IndexDocument {
    private String name;
    private java.util.List<ContentLoadRequest.ColumnProperty> columns;
    private List<String> values;

    public List<ContentLoadRequest.ColumnProperty> getColumns() {
        return columns;
    }

    public String getName() {
        return name;
    }

    public IndexDocument setName(String name) {
        this.name = name;
        return this;
    }

    public IndexDocument setColumns(List<ContentLoadRequest.ColumnProperty> columns) {
        this.columns = columns;
        return this;
    }

    public List<String> getValues() {
        return values;
    }

    public IndexDocument setValues(List<String> values) {
        this.values = values;
        return this;
    }
}
