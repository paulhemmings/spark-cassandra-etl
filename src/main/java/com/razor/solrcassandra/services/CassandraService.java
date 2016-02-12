package com.razor.solrcassandra.services;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.razor.solrcassandra.models.LoadDocument;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.razor.solrcassandra.models.LoadProperties.ColumnProperty;

/**
 * Created by paulhemmings on 10/19/15.
 * https://academy.datastax.com/demos/getting-started-apache-cassandra-and-java-part-i
 */

public class CassandraService {

    private Session session;

    public CassandraService() {

    }

    public String quoteColumn(String value, boolean quoteIt) {
        return quoteIt ? "'" + value + "'" : value;
    }

    public String buildHeader(List<ColumnProperty> columns) {
        return columns.stream()
                .map(ColumnProperty::getColumnName)
                .collect(Collectors.joining(","));
    }

    public String buildValues(List<ColumnProperty> columns, List<String> values) {
        return IntStream.range(0, columns.size())
                    .mapToObj(index -> this.quoteColumn(values.get(index), columns.get(index).isColumnQuoted()))
                    .collect(Collectors.joining(","));
    }

    public String buildCql(LoadDocument loadDocument) {
        return "INSERT INTO "
                + loadDocument.getName()
                + " ("
                + this.buildHeader(loadDocument.getColumns())
                + ") VALUES ("
                + this.buildValues(loadDocument.getColumns(), loadDocument.getValues())
                + ")";
    }


    public void insert(String cql) {
        this.session.execute(cql);
    }

    public void connect(String host, String keySpace) {
        this.session = Cluster.builder().addContactPoint(host).build().connect(keySpace);
    }

    public void disconnect() {
        if (!this.session.isClosed()) {
            this.session.close();
        }
    }

}
