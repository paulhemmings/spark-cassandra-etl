package com.razor.solrcassandra.models;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by paulhemmings on 10/19/15.
 */

public class LoadProperties {

    private String hostName;
    private String keySpace;

    private String tableName;
    private List<String> columnDefinitions;
    private String csvFileName;

    public String getHostName() {
        return hostName;
    }

    public String getKeySpace() {
        return keySpace;
    }

    public String getTableName() {
        return tableName;
    }

    public String getCsvFileName() {
        return csvFileName;
    }

    /*
     * For testing purposes only.
     */

    public void setColumnDefinitions(List<String> columnDefinitions) {
        this.columnDefinitions = columnDefinitions;
    }

    public List<ColumnProperty> buildColumns() {
        return this.columnDefinitions.stream()
                .map(this::buildColumnProperty)
                .collect(Collectors.toList());
    }

    private ColumnProperty buildColumnProperty(String columnDefinition) {
        String[] parts = columnDefinition.split(":");
        return new ColumnProperty()
                .setColumnName(parts[0])
                .setColumnQuoted(parts[1].equalsIgnoreCase("true"));
    }

    public static class ColumnProperty {
        private String columnName;
        private boolean columnQuoted;

        public String getColumnName() {
            return columnName;
        }

        public boolean isColumnQuoted() {
            return columnQuoted;
        }

        public ColumnProperty setColumnName(String columnName) {
            this.columnName = columnName;
            return this;
        }

        public ColumnProperty setColumnQuoted(boolean columnQuoted) {
            this.columnQuoted = columnQuoted;
            return this;
        }
    }
}
