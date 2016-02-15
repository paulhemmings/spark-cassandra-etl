package com.razor.solrcassandra.resources;

import com.google.gson.Gson;
import com.razor.solrcassandra.models.LoadDocument;
import com.razor.solrcassandra.models.LoadProperties;
import com.razor.solrcassandra.services.CassandraService;
import com.razor.solrcassandra.services.FileService;
import com.razor.solrcassandra.services.SolrService;
import com.razor.solrcassandra.utilities.JsonUtil;
import org.apache.solr.client.solrj.SolrClient;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static spark.Spark.post;

public class LoadResource extends BaseResource {

    private FileService fileService;
    private CassandraService cassandraService;
    private SolrService solrService;

    /**
     * Constructor - inject all dependencies
     * @param fileService
     * @param cassandraService
     */

    public LoadResource(FileService fileService, CassandraService cassandraService, SolrService solrService) {
        this.fileService = fileService;
        this.cassandraService = cassandraService;
        this.solrService = solrService;
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

        final List<LoadDocument> entries = new ArrayList<>();

        // get the load properties
        LoadProperties loadProperties = this.buildLoadProperties(request);

        // build the solrClient
        SolrClient solrClient = this.solrService.buildSolrClient(this.solrService.getFullUrl(loadProperties.getSolrCore()));

        // connect to Cassandra
        this.cassandraService.connect(loadProperties.getHostName(), loadProperties.getKeySpace());

        // load the data.
        this.fileService.loadData(loadProperties.getCsvFileName(), line -> {

            // build the load document
            LoadDocument loadDocument = this.buildLoadDocument(
                loadProperties.getTableName(),
                loadProperties.buildColumns(),
                Arrays.asList(line.split(","))
            );

            // load it into Cassandra
            String cql = this.cassandraService.buildCql(loadDocument);
            this.cassandraService.insert(cql);

            // load it into SOLR
            this.solrService.load(solrClient, loadDocument);

            // add to successful entry
            entries.add(loadDocument);

        });

        // disconnect from Cassandra
        this.cassandraService.disconnect();
        // close the SOLR client
        solrClient.close();

        // return something
        return new Gson().toJson(entries);
    }



}
