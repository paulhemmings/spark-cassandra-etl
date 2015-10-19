package com.razor.myDatastaxEtl.models;

import java.util.List;

/**
 * Created by paulhemmings on 10/19/15.
 */

public class LoadProperties {

    private String hostName;
    private String keySpace;

    private String tableName;
    private List<String> columns;
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

    public List<String> getColumns() {
        return columns;
    }

    public String[] getColumnArray() {
        String[] array = new String[this.columns.size()];
        return this.columns.toArray(array);
    }

    public String getCsvFileName() {
        return csvFileName;
    }
}
