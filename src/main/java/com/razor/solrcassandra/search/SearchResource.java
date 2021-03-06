package com.razor.solrcassandra.search;

import com.google.gson.Gson;
import com.razor.solrcassandra.content.ContentDocument;
import com.razor.solrcassandra.converters.RequestToContentDocument;
import com.razor.solrcassandra.converters.RequestToSearchParameters;
import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.models.RequestResponse;
import com.razor.solrcassandra.resources.BaseResource;
import com.razor.solrcassandra.utilities.JsonUtil;
import org.apache.solr.client.solrj.SolrServerException;
import spark.Request;
import spark.Response;
import spark.utils.StringUtils;

import java.io.IOException;

import static com.razor.solrcassandra.utilities.ExtendedUtils.orElse;
import static spark.Spark.get;
import static spark.Spark.post;

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
     * GET /search
     *   Accepts JSON
     *     http://localhost:4567/search/books-core?jq={%27cat%27:[%27book%27]}
     *   Or individual parameters
     *     http://localhost:4567/search/books-core?q=*:*&facets=cat,name,genre_s
     *
     * Index a new document inside SOLR.
     *
     * POST /search/index/:core
     *      [{ one:"item", two:"item" }]
     */


    private void setupEndpoints() {
        get("/search/:core", "application/json", this::handleSearchRequest, JsonUtil::toJson);
        post("/search/index/:core", "application/json", this::handleIndexRequest, JsonUtil::toJson);
    }

    /**
     * Handles the index request
     * @param request
     * @param response
     * @return
     * @throws ServiceException
     */

    private RequestResponse handleIndexRequest(Request request, Response response) throws ServiceException {
        response.type("application/json");

        String core = request.params(":core");
        if (StringUtils.isEmpty(core)) {
            throw new ServiceException("Failed to provide an index core for the document");
        }

        ContentDocument contentDocument = new RequestToContentDocument().convert(request);
        return this.searchService.index(core, contentDocument);
    }

    /**
     * Handle the Search Request
     * @param request
     * @param response
     * @return SearchResponse
     * @throws IOException
     * @throws SolrServerException
     */

    private RequestResponse handleSearchRequest(Request request, Response response) throws ServiceException {
        response.type("application/json");

        String core = request.params(":core");
        if (StringUtils.isEmpty(core)) {
            throw new ServiceException("Failed to provide an index core for the document");
        }

        SearchParameters searchParameters = new RequestToSearchParameters().convert(request);
        return this.searchService.query(core, searchParameters);
    }
}
