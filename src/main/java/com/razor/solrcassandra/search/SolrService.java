package com.razor.solrcassandra.search;

import com.razor.solrcassandra.builders.SolrQueryBuilder;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
        return new SolrQueryBuilder()
            .withFacet(true)
            .withFacetLimit(searchParameters.getFacetLimit())
            .withRows(searchParameters.getRows())
            .withQuery(searchParameters.getQuery())
            .withFilterQueries(searchParameters.getFilterQueries())
            .withIncludedFacets(searchParameters.getFacets())
            .withSort(searchParameters.getSort())
            .withOrder(searchParameters.getOrder().equals(SearchParameters.ASC) ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc)
            .build();
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

        this.withClient(this.host, core, solrClient -> {
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

    /**
     * Takes location of SOLR instance, and a method that takes the SolrClient as a parameter
     * This way the SolrClient object is never truly released. Once the method completes, the client closes
     * @param host
     * @param core
     * @param usingClient
     */

    public void withClient(String host, String core, Consumer<SolrClient> usingClient) {
        String url = String.format("%s/%s", host, core);
        SolrClient solrClient = this.buildClient(url); // new HttpSolrClient(url);
        usingClient.accept(solrClient);
        try {
            solrClient.close();
        } catch (IOException e) {
        }
    }

    /**
     * Separated out for unit testing
     * @return
     */

    protected QueryResponseToSearchResponse buildQueryResponseConverterInstance() {
        return new QueryResponseToSearchResponse();
    }

    /**
     * Separated out for unit testing
     * @return
     */

    protected ContentDocumentToSolrInputDocument buildContentDocumentConverterInstance() {
        return new ContentDocumentToSolrInputDocument();
    }

    /**
     * Builds an instance of the solr client. protected, and only separated at all so I can unit test the class
     * @param url
     * @return
     */

    protected SolrClient buildClient(String url) {
        return new HttpSolrClient(url);
    }

}
