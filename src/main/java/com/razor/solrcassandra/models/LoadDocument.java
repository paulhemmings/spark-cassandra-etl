package com.razor.solrcassandra.models;

import java.util.List;

/**
 * Created by paul.hemmings on 2/12/16.
 */

public class LoadDocument {
    private String name;
    private java.util.List<LoadProperties.ColumnProperty> columns;
    private List<String> values;

    public List<LoadProperties.ColumnProperty> getColumns() {
        return columns;
    }

    public String getName() {
        return name;
    }

    public LoadDocument setName(String name) {
        this.name = name;
        return this;
    }

    public LoadDocument setColumns(List<LoadProperties.ColumnProperty> columns) {
        this.columns = columns;
        return this;
    }

    public List<String> getValues() {
        return values;
    }

    public LoadDocument setValues(List<String> values) {
        this.values = values;
        return this;
    }
}
