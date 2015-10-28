package com.razor.myDatastaxEtl.resources;

import com.google.gson.Gson;
import com.razor.myDatastaxEtl.models.LoadProperties;
import com.razor.myDatastaxEtl.services.CassandraService;
import com.razor.myDatastaxEtl.services.FileService;
import spark.Request;

import java.io.IOException;
import java.util.Arrays;

import static com.razor.myDatastaxEtl.utilities.JsonUtil.json;
import static spark.Spark.*;

public class LoadResource {

    private FileService fileService;
    private CassandraService cassandraService;

    public LoadResource(FileService fileService, CassandraService cassandraService) {
        this.fileService = fileService;
        this.cassandraService = cassandraService;
        setupEndpoints();
    }

    private void setupEndpoints() {
        post("/load", "application/json", (request, response) -> {
            return this.handleLoadRequest(this.buildLoadProperties(request));
        }, json());
    }

    private LoadProperties buildLoadProperties(Request request) {
        return new Gson().fromJson(request.body(), LoadProperties.class);
    }

    private String handleLoadRequest(LoadProperties loadProperties) throws IOException {
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
