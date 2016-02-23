package com.razor.solrcassandra.content;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.Select;
import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.models.RequestResponse;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

/**
 * Created by paulhemmings on 10/19/15.
 * https://academy.datastax.com/demos/getting-started-apache-cassandra-and-java-part-i
 */

public class CassandraService implements ContentService {

    private final String host;

    public CassandraService(String hostUrl) {
        this.host = hostUrl;
    }

    /**
     * Insert a document into the store
     * @param keySpace
     * @param tableName
     * @param contentDocument
     * @return
     * @throws ServiceException
     */

    public RequestResponse insert(String keySpace, String tableName, ContentDocument contentDocument) throws ServiceException {
        this.withSession(host, keySpace, session -> {
            BuiltStatement insertStatement = this.buildInsertStatement(keySpace, tableName, contentDocument);
            PreparedStatement preparedStatement = session.prepare(insertStatement);
            this.withBoundRows(preparedStatement, contentDocument, session::execute);
        });
        return new RequestResponse();
    }

    /**
     * Retrieve Content from the Content Store
     * @param keySpace
     * @param filters
     * @return
     * @throws ServiceException
     */

    public RequestResponse<ContentDocument> retrieve(String keySpace, String tableName, Map<String, String> filters) throws ServiceException {
        ContentDocument document = new ContentDocument();
        this.withSession(host, keySpace, session -> {
            BuiltStatement retrieveStatement = this.buildRetrieveStatement(keySpace, tableName, filters);
            PreparedStatement preparedStatement = session.prepare(retrieveStatement);
            this.withBound(preparedStatement, filters.values(), session::execute);
        });
        return new RequestResponse<ContentDocument>().setResponseContent(document);
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
        for(Map<String, Object> row : contentDocument) {
            BoundStatement boundStatement = new BoundStatement(preparedStatement).bind(row.values());
            useStatement.accept(boundStatement);
        }
    }

    /**
     * Binds a prepared statement with the values
     * @param preparedStatement
     * @param values
     * @param useStatement
     */

    protected <V> void withBound(PreparedStatement preparedStatement, Collection<V> values, Consumer<BoundStatement> useStatement) {
        BoundStatement boundStatement = new BoundStatement(preparedStatement).bind(values);
        useStatement.accept(boundStatement);
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
        contentDocument.get(0).keySet().forEach(key -> {
            insertStatement.value(key, bindMarker());
        });
        return insertStatement;
    }

    /**
     * Builds a retrieve statement based on the filters
     * @param keySpace
     * @param tableName
     * @param filters
     * @return
     */

    protected BuiltStatement buildRetrieveStatement(String keySpace, String tableName, Map<String, String> filters) {
        Select select = select().from(keySpace, tableName);
        for (String key : filters.keySet()) {
            select.where(eq(key, bindMarker()));
        }
        return select;
    }



}
