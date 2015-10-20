package com.razor.myDatastaxEtl.services;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.util.Arrays;
import java.util.List;

/**
 * Created by paulhemmings on 10/19/15.
 * https://academy.datastax.com/demos/getting-started-apache-cassandra-and-java-part-i
 */

public class CassandraService {

    private Cluster cluster;
    private Session session;

    public CassandraService() {

    }

    public String join(Object[] columns, boolean quoteText) {
        StringBuilder builder = new StringBuilder();
        for (Object column : columns) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            String value = String.valueOf(column);
            if (column instanceof String && quoteText) {
                value = String.format("'%s'", column);
            }
            builder.append(value);
        }
        return builder.toString();
    }

    public void insert(String table, Object[] columns, Object[] values) {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ");
        builder.append(table);
        builder.append("(");
        builder.append(this.join(columns, false));
        builder.append(") VALUES (");
        builder.append(this.join(values, false));
        builder.append(")");
        session.execute(builder.toString());
    }

    public void connect(String host, String keySpace) {
        this.cluster = Cluster.builder().addContactPoint(host).build();
        this.session = this.cluster.connect(keySpace);
    }

    public void disconnect() {
        if (!this.session.isClosed()) {
            this.session.close();
        }
    }

}
