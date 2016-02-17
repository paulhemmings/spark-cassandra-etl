package com.razor.solrcassandra.content;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by paulhemmings on 10/19/15.
 */

public class ContentLoadRequest {

    private String hostName;
    private String keySpace;
    private String tableName;

    private List<String> columns;
    private String csvFileName;

    public String getHostName() {
        return this.hostName;
    }

    public String getKeySpace() {
        return this.keySpace;
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getCsvFileName() {
        return this.csvFileName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setKeySpace(String keySpace) {
        this.keySpace = keySpace;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public void setCsvFileName(String csvFileName) {
        this.csvFileName = csvFileName;
    }
}
