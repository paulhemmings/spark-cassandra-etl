package com.razor.solrcassandra.resources;

import com.google.gson.Gson;
import com.razor.solrcassandra.models.LoadProperties;
import com.razor.solrcassandra.services.CassandraService;
import com.razor.solrcassandra.services.FileService;
import com.razor.solrcassandra.utilities.JsonUtil;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Arrays;

import static spark.Spark.post;

public class LoadResource extends BaseResource {

    private FileService fileService;
    private CassandraService cassandraService;

    /**
     * Constructor - inject all dependencies
     * @param fileService
     * @param cassandraService
     */

    public LoadResource(FileService fileService, CassandraService cassandraService) {
        this.fileService = fileService;
        this.cassandraService = cassandraService;
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
     * Handle load request
     * @param request
     * @param response
     * @return success/failure message
     * @throws IOException
     */

    private String handleLoadRequest(Request request, Response response) throws IOException {
        LoadProperties loadProperties = this.buildLoadProperties(request);
        this.cassandraService.connect(loadProperties.getHostName(), loadProperties.getKeySpace());
        this.fileService.loadData(loadProperties.getCsvFileName(), line -> {
            String cql = this.cassandraService.buildCql(
                    loadProperties.getTableName(),
                    loadProperties.buildColumns(),
                    Arrays.asList(line.split(",")));
            this.cassandraService.insert(cql);
        });
        this.cassandraService.disconnect();
        return "success";
    }

}
