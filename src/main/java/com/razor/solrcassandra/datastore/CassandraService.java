package com.razor.solrcassandra.datastore;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.SyntaxError;
import com.razor.solrcassandra.load.LoadDocument;
import com.razor.solrcassandra.load.LoadProperties;
import com.razor.solrcassandra.load.LoadResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.razor.solrcassandra.load.LoadProperties.ColumnProperty;

/**
 * Created by paulhemmings on 10/19/15.
 * https://academy.datastax.com/demos/getting-started-apache-cassandra-and-java-part-i
 */

public class CassandraService implements StoreService {

    private Session session;

    public void connect(LoadProperties loadProperties) {
        String host = loadProperties.getHostName();
        String keySpace = loadProperties.getKeySpace();
        this.session = Cluster.builder().addContactPoint(host).build().connect(keySpace);
    }

    public void disconnect() {
        if (!this.session.isClosed()) {
            this.session.close();
        }
    }

    public LoadResponse insert(LoadDocument loadDocument) {
        String cql = this.buildCql(loadDocument);
        LoadResponse loadResponse = new LoadResponse().setLoadStatistics(cql);
        try {
            this.session.execute(cql);
        } catch (SyntaxError ex) {
            loadResponse.setErrorMessage(ex.getMessage());
        }
        return loadResponse;

    }

    protected String quoteColumn(String value, boolean quoteIt) {
        return quoteIt ? "'" + value + "'" : value;
    }

    protected String buildHeader(List<ColumnProperty> columns) {
        return columns.stream()
                .map(columnProperty -> "\"" + columnProperty.getColumnName().toUpperCase() + "\"")
                .collect(Collectors.joining(","));
    }

    protected String buildValues(List<ColumnProperty> columns, List<String> values) {
        return IntStream.range(0, columns.size())
                    .mapToObj(index -> this.quoteColumn(values.get(index), columns.get(index).isColumnQuoted()))
                    .collect(Collectors.joining(","));
    }

    protected String buildCql(LoadDocument loadDocument) {
        return "INSERT INTO "
                + loadDocument.getName()
                + " ("
                + this.buildHeader(loadDocument.getColumns())
                + ") VALUES ("
                + this.buildValues(loadDocument.getColumns(), loadDocument.getValues())
                + ")";
    }



}
