package com.razor.solrcassandra.resources;

import com.google.gson.Gson;
import com.razor.solrcassandra.models.SearchRequest;
import com.razor.solrcassandra.models.SearchResponse;
import com.razor.solrcassandra.services.SolrService;
import com.razor.solrcassandra.utilities.JsonUtil;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import spark.Request;
import spark.Response;

import java.io.IOException;

import static com.razor.solrcassandra.utilities.JsonUtil.json;
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
     */

    private void setupEndpoints() {
        // http://localhost:4567/search?core=books-core&q={%27cat%27:[%27book%27]}
        get("/search", "application/json", this::handleSearchRequest, JsonUtil::toJson); //  json()
    }

    /**
     * Returns the name of the specified SOLR core - defaults to "master" if none provided
     * @param request
     * @return
     */

    private String getCore(Request request) {
        if (!request.queryParams().contains("core")) {
            return "master";
        }
        return request.queryMap("core").value();
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
        SolrQuery solrQuery = this.buildSearchQuery(request);
        SolrClient solrClient = this.solrService.buildSolrClient(this.solrService.getFullUrl(core));
        SearchResponse solrResponse = this.solrService.query(solrClient, solrQuery);
        solrClient.close();
        return solrResponse;
    }

    /**
     * Build a SolrQuery object from the query parameters (q)
     * @param request
     * @return
     */

    private SolrQuery buildSearchQuery(Request request) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setFacet(true);
        solrQuery.setFacetLimit(1000);
        solrQuery.set("q","*:*");
        SearchRequest searchRequest = new Gson().fromJson(request.queryMap("q").value(), SearchRequest.class);
        searchRequest.keySet().stream().forEach(key -> solrQuery.addFilterQuery(key + ":" + searchRequest.valueList(key)));
        return solrQuery;
    }

}
