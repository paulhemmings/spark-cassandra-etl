package com.razor.solrcassandra.content;

import com.google.gson.Gson;
import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.load.FileLoaderService;
import com.razor.solrcassandra.models.RequestResponse;
import com.razor.solrcassandra.resources.BaseResource;
import com.razor.solrcassandra.utilities.JsonUtil;
import spark.Request;
import spark.Response;

import java.util.*;

import static spark.Spark.post;
import static spark.Spark.get;

/**
 * Created by paul.hemmings on 2/15/16.
 */
public class ContentResource extends BaseResource {

    private ContentService contentService;

    /**
     * Constructor - inject all dependencies
     * @param contentService
     */

    public ContentResource(ContentService contentService) {
        this.contentService = contentService;
        setupEndpoints();
    }

    /**
     * Sets up the service end points
     */

    private void setupEndpoints() {
        post("/load", "application/json", this::handleLoadRequest, JsonUtil::toJson);
        get("/retrieve", "application/json", this::handleRetrieveRequest, JsonUtil::toJson);
    }

    /**
     * Build a ContentRetrieveRequest object from the Spark HTTP request object
     * @param request
     * @return
     */

    private ContentRetrieveRequest buildContentRetrieveRequest(Request request) {
        return new ContentRetrieveRequest();
    }

    /**
     * Builds a ContentLoadRequest model from the request body
     * @param request
     * @return
     */

    private ContentLoadRequest buildContentLoadRequest(Request request) {
        return new Gson().fromJson(request.body(), ContentLoadRequest.class);
    }

    /**
     * Build the index document from the Load Properties (spot the obvious future improvement here!!)
     * @param name
     * @param columns
     * @param values
     * @return
     */

    private ContentDocument buildContentDocument(String name, List<ContentLoadRequest.ColumnProperty> columns, List<String> values) {
        ContentDocument contentDocument = new ContentDocument().setName(name);
        ContentDocument.ContentRow contentRow = contentDocument.createRow();

        int index = 0;
        for (ContentLoadRequest.ColumnProperty column : columns) {
            String value = column.isColumnQuoted() ? "'" + values.get(index) + "'" : values.get(index);
            contentRow.add(new ContentDocument.ContentCell().setColumnName(column.getColumnName()).setColumnValue(value));
            index ++;
        }
        return contentDocument;
    }

    /**
     *
     * @param request
     * @param response
     * @return
     * @throws ServiceException
     */

    private RequestResponse<ContentDocument> handleRetrieveRequest(Request request, Response response) throws ServiceException {
        ContentRetrieveRequest retrieveRequest = this.buildContentRetrieveRequest(request);
        return this.contentService.retrieve(retrieveRequest);
    }

    /**
     * Handle index request
     * @param request
     * @param response
     * @return success/failure message
     * @throws ServiceException
     */

    private List<RequestResponse<ContentDocument>> handleLoadRequest(Request request, Response response) throws ServiceException {

        final List<RequestResponse<ContentDocument>> entries = new ArrayList<>();

        // get the index properties
        ContentLoadRequest contentLoadRequest = this.buildContentLoadRequest(request);

        // connect to Cassandra
        this.contentService.connect(contentLoadRequest);

        // index the data.
        new FileLoaderService().loadData(contentLoadRequest.getCsvFileName(), line -> {

            RequestResponse<ContentDocument> requestResponse = null;

            // build the content document
            ContentDocument loadDocument = this.buildContentDocument(
                    contentLoadRequest.getTableName(),
                    contentLoadRequest.buildColumns(),
                    Arrays.asList(line.split(","))
            );

            // load it into content store
            try {
                requestResponse = this.contentService.insert(loadDocument);
            } catch (ServiceException e) {
                requestResponse = new RequestResponse<>().setErrorMessage(e.getMessage());
            }

            // add to successful entry
            entries.add(requestResponse);

        });

        // disconnect from Cassandra
        this.contentService.disconnect();

        // return response
        return entries;
    }

}
