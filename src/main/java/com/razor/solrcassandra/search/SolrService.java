package com.razor.solrcassandra.search;

import com.razor.solrcassandra.content.ContentDocument;
import com.razor.solrcassandra.converters.ContentDocumentToSolrInputDocument;
import com.razor.solrcassandra.converters.QueryResponseToSearchResponse;
import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.models.RequestResponse;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import spark.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.razor.solrcassandra.utilities.ExtendedUtils.orElse;

/**
 * Created by paul.hemmings on 2/11/16.
 */

public class SolrService implements SearchService {

    private final String host;

    public SolrService(String hostUrl) {
        this.host = hostUrl;
    }

    /**
     * Build SolrQuery from the generic search parameters
     * Set the default values here
     * @param searchParameters
     * @return
     */

    public SolrQuery buildSearchQuery(SearchParameters searchParameters) {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setFacet(true);
        solrQuery.setFacetLimit(Integer.valueOf(orElse(searchParameters.getFacetLimit(), "100")));
        solrQuery.setRows(Integer.valueOf(orElse(searchParameters.getRows(), "40")));
        solrQuery.set("q", orElse(searchParameters.getQuery(), "*:*"));
        searchParameters.getFilterQueries().stream().forEach(solrQuery::addFilterQuery);
        searchParameters.getFacets().stream().forEach(solrQuery::addFacetField);
        if (StringUtils.isNotEmpty(searchParameters.getSort())) {
            solrQuery.setSort(searchParameters.getSort(), searchParameters.getOrder().equals(SearchParameters.ASC) ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc);
        }
        return solrQuery;
    }

    /**
     * Build a generic SearchResponse from the Solr response
     * @param searchParameters
     * @return
     * @throws IOException
     * @throws SolrServerException
     */

    public RequestResponse<SearchResponse> query(String core, SearchParameters searchParameters) throws ServiceException {
        RequestResponse<SearchResponse> response = new RequestResponse<>();
        this.withClient(this.host, core, solrClient -> {
            SolrQuery solrQuery = this.buildSearchQuery(searchParameters);
            try {
                QueryResponse queryResponse = solrClient.query(solrQuery);
                response.setResponseContent(this.buildQueryResponseConverterInstance().convert(queryResponse));
            } catch (SolrServerException | IOException e) {
                response.setErrorMessage(e.getMessage());
            }
        });
        return response;
    }

    /**
     * Add a content document to the index
     * @param contentDocument
     * @return RequestResponse
     */

    public RequestResponse<List<NamedList<Object>>> index(String core, ContentDocument contentDocument) throws ServiceException {

        ContentDocumentToSolrInputDocument converter = this.buildContentDocumentConverterInstance();
        RequestResponse<List<NamedList<Object>>> requestResponse = new RequestResponse<>();
        requestResponse.setResponseContent(new ArrayList<>());

        this.withClient(this.getHost(), core, solrClient -> {
            for (Map<String, Object> row : contentDocument) {
                SolrInputDocument inputDocument = converter.convert(row);
                try {
                    solrClient.add(inputDocument);
                    UpdateResponse updateResponse = solrClient.commit();
                    requestResponse.getResponseContent().add(updateResponse.getResponse());
                } catch (SolrServerException | IOException | HttpSolrClient.RemoteSolrException e) {
                    requestResponse.setErrorMessage(e.getMessage());
                }
            }
        });

        return requestResponse;
    }

    public void withClient(String host, String core, Consumer<SolrClient> usingClient) {
        String url = String.format("%s/%s", host, core);
        SolrClient solrClient = this.buildClient(url); // new HttpSolrClient(url);
        usingClient.accept(solrClient);
        try {
            solrClient.close();
        } catch (IOException e) {
        }
    }

    public QueryResponseToSearchResponse buildQueryResponseConverterInstance() {
        return new QueryResponseToSearchResponse();
    }

    public ContentDocumentToSolrInputDocument buildContentDocumentConverterInstance() {
        return new ContentDocumentToSolrInputDocument();
    }

    public String getHost() {
        return this.host;
    }

    protected SolrClient buildClient(String url) {
        return new HttpSolrClient(url);
    }

}
