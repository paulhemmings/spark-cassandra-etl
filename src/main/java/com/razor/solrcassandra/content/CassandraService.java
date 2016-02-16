package com.razor.solrcassandra.content;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.SyntaxError;
import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.models.RequestResponse;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by paulhemmings on 10/19/15.
 * https://academy.datastax.com/demos/getting-started-apache-cassandra-and-java-part-i
 */

public class CassandraService implements ContentService {

    private Session session;

    public void connect(ContentLoadRequest contentLoadRequest) {
        String host = contentLoadRequest.getHostName();
        String keySpace = contentLoadRequest.getKeySpace();
        this.session = Cluster.builder().addContactPoint(host).build().connect(keySpace);
    }

    public void disconnect() {
        if (!this.session.isClosed()) {
            this.session.close();
        }
    }

    /**
     * Insert Content into the Content Store
     * @param contentDocument
     * @return
     */

    public RequestResponse<ContentDocument> insert(ContentDocument contentDocument) throws ServiceException {

        RequestResponse<ContentDocument> requestResponse = new RequestResponse<>();

        if (Objects.isNull(this.session)) {
            throw new ServiceException("No connection to content store");
        }

        for(ContentDocument.ContentRow contentRow : contentDocument.rows()) {
            String cql = this.buildCql(contentDocument.getName(), contentRow);
            try {
                this.session.execute(cql);
            } catch (SyntaxError ex) {
                requestResponse.setErrorMessage(ex.getMessage());
            }
        }

        return requestResponse;
    }

    /**
     * Retrieve Content from the Content Store
     * @param request
     * @return
     */

    public RequestResponse<ContentDocument> retrieve(ContentRetrieveRequest request) throws ServiceException {
        if (Objects.isNull(this.session)) {
            throw new ServiceException("No connection to content store");
        }
        return null;
    }

    /**
     * Build the CQL header row
     * @param contentRow
     * @return
     */

    protected String buildHeader(ContentDocument.ContentRow contentRow) {
        return contentRow.stream()
                .map(contentCell -> "\"" + contentCell.getColumnName().toUpperCase() + "\"")
                .collect(Collectors.joining(","));
    }

    /**
     * Build the CQL values row
     * @param contentRow
     * @return
     */

    protected String buildValues(ContentDocument.ContentRow contentRow) {
        return contentRow.stream()
                .map(contentCell -> String.valueOf(contentCell.getColumnValue()))
                .collect(Collectors.joining(","));
//        return IntStream.range(0, contentDocument.rows().size())
//                    .mapToObj(index -> this.quoteColumn(values.get(index), columns.get(index).isColumnQuoted()))
//                    .collect(Collectors.joining(","));
    }

    /**
     * Build the CQL
     * @param contentRow
     * @return
     */

    protected String buildCql(String contentName, ContentDocument.ContentRow contentRow) {
        return "INSERT INTO "
                + contentName
                + " ("
                + this.buildHeader(contentRow)
                + ") VALUES ("
                + this.buildValues(contentRow)
                + ")";
    }



}
