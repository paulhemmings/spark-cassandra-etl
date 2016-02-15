package com.razor.solrcassandra.load;

import com.google.gson.Gson;
import com.razor.solrcassandra.datastore.StoreService;
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
    private StoreService storeService;
    private SearchService searchService;

    /**
     * Constructor - inject all dependencies
     * @param fileService
     * @param storeService
     * @param searchService
     */

    public LoadResource(FileLoaderService fileService, StoreService storeService, SearchService searchService) {
        this.fileService = fileService;
        this.storeService = storeService;
        this.searchService = searchService;
        setupEndpoints();
    }

    /**
     * Sets up the service end points
     */

    private void setupEndpoints() {
        post("/load", "application/json", this::handleLoadRequest, JsonUtil::toJson);
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
     * Build the load document from the Load Properties (spot the obvious future improvement here!!)
     * @param name
     * @param columns
     * @param values
     * @return
     */

    private LoadDocument buildLoadDocument(String name, List<LoadProperties.ColumnProperty> columns, List<String> values) {
        return new LoadDocument().setColumns(columns).setName(name).setValues(values);
    }

    /**
     * Handle load request
     * @param request
     * @param response
     * @return success/failure message
     * @throws IOException
     */

    private String handleLoadRequest(Request request, Response response) throws IOException {

        final List<Map<String, LoadResponse>> entries = new ArrayList<>();

        // get the load properties
        LoadProperties loadProperties = this.buildLoadProperties(request);

        // build the solrClient
        this.searchService.connect(loadProperties.getSearchIndex());

        // connect to Cassandra
        this.storeService.connect(loadProperties);

        // load the data.
        this.fileService.loadData(loadProperties.getCsvFileName(), line -> {

            Map<String, LoadResponse> responseMap = new HashMap<>();

            // build the load document
            LoadDocument loadDocument = this.buildLoadDocument(
                loadProperties.getTableName(),
                loadProperties.buildColumns(),
                Arrays.asList(line.split(","))
            );

            // load it into Cassandra
            responseMap.put("Inserting", this.storeService.insert(loadDocument));

            // load it into SOLR
            if (responseMap.get("Inserting").isSuccessful()) {
                responseMap.put("Indexing", this.searchService.load(loadDocument));
            }

            // add to successful entry
            entries.add(responseMap);

        });

        // disconnect from Cassandra
        this.storeService.disconnect();

        // close the SOLR client
        this.searchService.disconnect();

        // return something
        return new Gson().toJson(entries);
    }

}
