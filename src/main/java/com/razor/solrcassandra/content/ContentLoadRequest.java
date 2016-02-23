package com.razor.solrcassandra.content;

/**
 * Created by paulhemmings on 10/19/15.
 */

public class ContentLoadRequest {

    private String keySpace;
    private String tableName;
    private String csvFileName;

    public String getKeySpace() {
        return keySpace;
    }

    public void setKeySpace(String keySpace) {
        this.keySpace = keySpace;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCsvFileName() {
        return csvFileName;
    }

    public void setCsvFileName(String csvFileName) {
        this.csvFileName = csvFileName;
    }
}
