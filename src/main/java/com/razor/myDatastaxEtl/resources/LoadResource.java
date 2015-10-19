package com.razor.myDatastaxEtl.resources;

import com.google.gson.Gson;
import com.razor.myDatastaxEtl.models.LoadProperties;
import com.razor.myDatastaxEtl.services.CassandraService;
import com.razor.myDatastaxEtl.services.FileService;
import spark.Request;

import java.io.IOException;

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
        this.cassandraService.connect(loadProperties.getKeySpace(), loadProperties.getHostName());
        this.fileService.loadData(loadProperties.getCsvFileName(), new FileService.FileServiceCaller() {
            @Override
            public String handleLine(String line) {
                String[] values = line.split(",");
                LoadResource.this.cassandraService.insert(loadProperties.getTableName(), loadProperties.getColumnArray(), values);
                return null;
            }
        });
        this.cassandraService.disconnect();
        return "success";
    }

}
