package com.razor.solrcassandra.content;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.Insert;
import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.models.RequestResponse;

import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.datastax.driver.core.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;

/**
 * Created by paulhemmings on 10/19/15.
 * https://academy.datastax.com/demos/getting-started-apache-cassandra-and-java-part-i
 */

public class CassandraService implements ContentService {

    /**
     * Insert a document into the store
     * @param host
     * @param keySpace
     * @param tableName
     * @param contentDocument
     * @return
     * @throws ServiceException
     */

    public RequestResponse insert(String host, String keySpace, String tableName, ContentDocument contentDocument) throws ServiceException {
        this.withSession(host, keySpace, session -> {
            BuiltStatement insertStatement = this.buildInsertStatement(keySpace, tableName, contentDocument);
            PreparedStatement preparedStatement = session.prepare(insertStatement);
            this.withBoundRows(preparedStatement, contentDocument, session::execute);
        });
        return new RequestResponse();
    }

    /**
     * Retrieve Content from the Content Store
     * @param host
     * @param keySpace
     * @param filters
     * @return
     * @throws ServiceException
     */

    public RequestResponse<ContentDocument> retrieve(String host, String keySpace, Map<String, String> filters) throws ServiceException {
        ContentDocument document = new ContentDocument();
        this.withSession(host, keySpace, session -> {

        });
        return new RequestResponse<ContentDocument>().setResponseContent(document);
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
    }

    /**
     * Retrieve a session. Accepts a consumer that will use the session. Once finished, the session closes.
     * @param host
     * @param keySpace
     * @param connected
     */

    protected void withSession(String host, String keySpace, Consumer<Session> connected) {
        Session session = Cluster.builder().addContactPoint(host).build().connect(keySpace);
        connected.accept(session);
        if (!session.isClosed()) session.close();
    }

    /**
     * Takes a prepared statement. Binds that statement to each row within the content document. Returns the bound statement for that row
     * @param preparedStatement
     * @param contentDocument
     * @param useStatement
     */

    protected void withBoundRows(PreparedStatement preparedStatement, ContentDocument contentDocument, Consumer<BoundStatement> useStatement) {
        for(ContentDocument.ContentRow row : contentDocument.rows()) {
            BoundStatement boundStatement = new BoundStatement(preparedStatement).bind(row.stream().map(ContentDocument.ContentCell::getColumnValue));
            useStatement.accept(boundStatement);
        }
    }

    /**
     * Builds an insert statement for a defined content document
     * @param keySpace
     * @param tableName
     * @param contentDocument
     * @return
     */

    protected BuiltStatement buildInsertStatement(String keySpace, String tableName, ContentDocument contentDocument) {
        Insert insertStatement = insertInto(keySpace, tableName);
        for(ContentDocument.ContentCell contentCell : contentDocument.rows().get(0)) {
            insertStatement.value(contentCell.getColumnName(), bindMarker());
        }
        return insertStatement;
    }



}
