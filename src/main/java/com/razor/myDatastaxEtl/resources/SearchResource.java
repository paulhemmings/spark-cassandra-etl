package com.razor.myDatastaxEtl.resources;

import com.google.gson.Gson;
import com.razor.myDatastaxEtl.models.SearchRequest;
import com.razor.myDatastaxEtl.models.SearchResponse;
import com.razor.myDatastaxEtl.services.SolrService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import spark.Request;

import java.io.IOException;

import static com.razor.myDatastaxEtl.utilities.JsonUtil.json;
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

        get("/search", (request, response) -> {
            return this.handleQueryRequest(getCore(request), this.buildSearchQuery(request));
        }, json());

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
     * Build a SolrQuery object from the query parameters (q)
     * @param request
     * @return
     */

    private SolrQuery buildSearchQuery(Request request) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setFacet(true);
        solrQuery.setFacetLimit(1000);
        solrQuery.set("q","*:*");
        solrQuery.set("fl", "name"); // obviously not generic!!
        SearchRequest searchRequest = new Gson().fromJson(request.queryMap("q").value(), SearchRequest.class);
        searchRequest.keySet().stream().forEach(key -> solrQuery.addFilterQuery(key + ":" + searchRequest.valueList(key)));
        return solrQuery;
    }

    /**
     * Returns SearchResponse object inflated from querying SOLR
     * @param solrQuery
     * @return
     * @throws IOException
     * @throws SolrServerException
     */

    private SearchResponse handleQueryRequest(String core, SolrQuery solrQuery) throws IOException, SolrServerException {
        SolrClient solrClient = this.solrService.buildSolrClient(this.solrService.getCoreUrl(core));
        SearchResponse solrResponse = this.solrService.query(solrClient, solrQuery);
        solrClient.close();
        return solrResponse;
    }

}
