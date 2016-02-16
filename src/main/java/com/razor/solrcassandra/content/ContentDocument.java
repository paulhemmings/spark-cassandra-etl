package com.razor.solrcassandra.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public ContentDocument addContentRow(ContentRow row) {
        this.rows().add(row);
        return this;
    }

    public String getName() {
        return name;
    }

    public ContentDocument setName(String name) {
        this.name = name;
        return this;
    }

    public static class ContentRow extends ArrayList<ContentCell> {
        public ContentRow add(String name, Object value) {
            this.add(new ContentCell().setColumnName(name).setColumnValue(value));
            return this;
        }
    }

    public static class ContentCell {
        private String columnName;
        private Object columnValue;

        public String getColumnName() {
            return columnName;
        }

        public ContentCell setColumnName(String columnName) {
            this.columnName = columnName;
            return this;
        }

        public Object getColumnValue() {
            return columnValue;
        }

        public ContentCell setColumnValue(Object columnValue) {
            this.columnValue = columnValue;
            return this;
        }
    }
}
