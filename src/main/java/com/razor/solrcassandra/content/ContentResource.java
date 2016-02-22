package com.razor.solrcassandra.content;

import com.google.gson.Gson;
import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.load.FileLoaderService;
import com.razor.solrcassandra.models.RequestResponse;
import com.razor.solrcassandra.resources.BaseResource;
import com.razor.solrcassandra.utilities.JsonUtil;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static spark.Spark.get;
import static spark.Spark.post;

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
        post("/content/load", "application/json", this::handleLoadRequest, JsonUtil::toJson);
        get("/content/retrieve", "application/json", this::handleRetrieveRequest, JsonUtil::toJson);
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
     *
     * @param request
     * @param response
     * @return
     * @throws ServiceException
     */

    private RequestResponse<ContentDocument> handleRetrieveRequest(Request request, Response response) throws ServiceException {
        String host = request.params("host");
        String keySpace = request.params("keyspace");
        String tableName = request.params("tableName");
        Map<String, String> filters = request.params();
        return this.contentService.retrieve(host, keySpace, tableName, filters);
    }

    /**
     * Handle index request
     * @param request
     * @param response
     * @return success/failure message
     * @throws ServiceException
     */

    private RequestResponse<ContentDocument> handleLoadRequest(Request request, Response response) throws ServiceException {

        ContentLoadRequest contentLoadRequest = this.buildContentLoadRequest(request);
        ContentDocument contentDocument = new ContentDocument();

        // Build the content document
        new FileLoaderService().loadData(contentLoadRequest.getCsvFileName(), line -> {
            List<String> values = Arrays.asList(line.split(","));
            IntStream.range(0, values.size()).forEach(index -> {
                contentDocument.addContentRow(contentLoadRequest.getColumns().get(index), values.get(index));
            });
        });

        // load the content document
        this.contentService.insert(contentLoadRequest.getHostName(), contentLoadRequest.getKeySpace(), contentLoadRequest.getTableName(), contentDocument);

        // return response
        return new RequestResponse<ContentDocument>().setResponseContent(contentDocument);
    }

}
