package com.razor.solrcassandra.load;

import com.google.gson.Gson;
import com.razor.solrcassandra.content.ContentService;
import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.resources.BaseResource;
import com.razor.solrcassandra.search.SearchService;
import com.razor.solrcassandra.utilities.JsonUtil;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.*;

import static spark.Spark.post;

public class LoadResource extends BaseResource {

    private FileLoaderService fileService;
    private ContentService contentService;
    private SearchService searchService;

    /**
     * Constructor - inject all dependencies
     * @param fileService
     * @param contentService
     * @param searchService
     */

    public LoadResource(FileLoaderService fileService, ContentService contentService, SearchService searchService) {
        this.fileService = fileService;
        this.contentService = contentService;
        this.searchService = searchService;
        setupEndpoints();
    }

    /**
     * Sets up the service end points
     */

    private void setupEndpoints() {
        post("/index", "application/json", this::handleLoadRequest, JsonUtil::toJson);
    }

    /**
     * Builds a LoadProperties model from the request body
     * @param request
     * @return
     */

    private LoadProperties buildLoadProperties(Request request) {
        return new Gson().fromJson(request.body(), LoadProperties.class);
    }

    /**
     * Build the index document from the Load Properties (spot the obvious future improvement here!!)
     * @param name
     * @param columns
     * @param values
     * @return
     */

    private LoadDocument buildLoadDocument(String name, List<LoadProperties.ColumnProperty> columns, List<String> values) {
        return new LoadDocument().setColumns(columns).setName(name).setValues(values);
    }

    /**
     * Handle index request
     * @param request
     * @param response
     * @return success/failure message
     * @throws IOException
     */

    private String handleLoadRequest(Request request, Response response) throws ServiceException {

        final List<Map<String, LoadResponse>> entries = new ArrayList<>();

        // get the index properties
        LoadProperties loadProperties = this.buildLoadProperties(request);

        // build the solrClient
        this.searchService.connect(loadProperties.getSearchIndex());

        // connect to Cassandra
        this.contentService.connect(loadProperties);

        // index the data.
        this.fileService.loadData(loadProperties.getCsvFileName(), line -> {

            Map<String, LoadResponse> responseMap = new HashMap<>();

            // build the index document
            LoadDocument loadDocument = this.buildLoadDocument(
                loadProperties.getTableName(),
                loadProperties.buildColumns(),
                Arrays.asList(line.split(","))
            );

            try {

                // index it into Cassandra
                responseMap.put("Inserting", this.contentService.insert(loadDocument));

                // index it into SOLR
                responseMap.put("Indexing", this.searchService.index(loadDocument));

            } catch (ServiceException se) {
                // Record failure
                responseMap.put("Error", new LoadResponse().setErrorMessage(se.getMessage()));
            }

            // add to successful entry
            entries.add(responseMap);

        });

        // disconnect from Cassandra
        this.contentService.disconnect();

        // close the SOLR client
        this.searchService.disconnect();

        // return something
        return new Gson().toJson(entries);
    }

}
