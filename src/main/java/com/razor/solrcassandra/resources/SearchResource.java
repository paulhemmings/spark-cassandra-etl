package com.razor.solrcassandra.resources;

import com.razor.solrcassandra.converters.RequestToSearchParameters;
import com.razor.solrcassandra.models.SearchParameters;
import com.razor.solrcassandra.models.SearchResponse;
import com.razor.solrcassandra.services.SolrService;
import com.razor.solrcassandra.utilities.JsonUtil;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import spark.Request;
import spark.Response;

import java.io.IOException;

import static com.razor.solrcassandra.utilities.ExtendedUtils.orElse;
import static spark.Spark.get;

/**
 * Created by paul.hemmings on 2/11/16.
 */

public class SearchResource {

    private SolrService solrService;

    public SearchResource(SolrService solrService) {
        this.solrService = solrService;
        setupEndpoints();
    }

    /**
     * Sets up the service end points
     *
     * /search
     *   Accepts JSON
     *     http://localhost:4567/search/books-core?jq={%27cat%27:[%27book%27]}
     *   Or individual parameters
     *     http://localhost:4567/search/books-core?q=*:*&facets=cat,name,genre_s
     *
     */

    private void setupEndpoints() {
        get("/search/:core", "application/json", this::handleSearchRequest, JsonUtil::toJson);
    }

    /**
     * Handle the Search Request
     * @param request
     * @param response
     * @return SearchResponse
     * @throws IOException
     * @throws SolrServerException
     */

    private SearchResponse handleSearchRequest(Request request, Response response) throws IOException, SolrServerException {
        String core = getCore(request);
        SearchParameters searchParameters = new RequestToSearchParameters().convert(request);
        SolrClient solrClient = this.solrService.buildSolrClient(this.solrService.getFullUrl(core));
        SearchResponse solrResponse = this.solrService.query(solrClient, searchParameters);
        solrClient.close();
        return solrResponse;
    }

    /**
     * Returns the name of the specified SOLR core - defaults to "master" if none provided
     * @param request
     * @return
     */

    private String getCore(Request request) {
        return orElse(request.params(":core"), "master");
    }

}
