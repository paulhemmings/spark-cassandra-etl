package com.razor.solrcassandra.search;

import com.razor.solrcassandra.converters.RequestToSearchParameters;
import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.resources.BaseResource;
import com.razor.solrcassandra.utilities.JsonUtil;
import org.apache.solr.client.solrj.SolrServerException;
import spark.Request;
import spark.Response;

import java.io.IOException;

import static spark.Spark.get;

/**
 * Created by paul.hemmings on 2/11/16.
 */

public class SearchResource extends BaseResource {

    private SearchService searchService;

    /**
     * Constructor - inject dependencies
     * @param searchService
     */

    public SearchResource(SearchService searchService) {
        this.searchService = searchService;
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

    private SearchResponse handleSearchRequest(Request request, Response response) throws ServiceException {
        response.type("application/json");
        SearchParameters searchParameters = new RequestToSearchParameters().convert(request);
        this.searchService.connect(searchParameters.getSearchIndex());
        SearchResponse solrResponse = this.searchService.query(searchParameters);
        this.searchService.disconnect();
        return solrResponse;
    }

}
