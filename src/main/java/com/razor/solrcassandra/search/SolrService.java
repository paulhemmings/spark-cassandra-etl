package com.razor.solrcassandra.search;

import com.razor.solrcassandra.converters.LoadDocumentToSolrInputDocument;
import com.razor.solrcassandra.converters.QueryResponseToSearchResponse;
import com.razor.solrcassandra.load.LoadDocument;
import com.razor.solrcassandra.load.LoadResponse;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import spark.utils.StringUtils;

import java.io.IOException;
import java.util.Objects;

import static com.razor.solrcassandra.utilities.ExtendedUtils.orElse;

/**
 * Created by paul.hemmings on 2/11/16.
 */

public class SolrService implements SearchService {

    private static String SERVER_URL = "http://localhost:8983/solr";
    private SolrClient solrClient = null;

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
     * Connect
     * @param searchIndex
     */

    public void connect(String searchIndex) {
        this.solrClient = this.buildSolrClient(this.getFullUrl(searchIndex));
    }

    /**
     * Disconnect
     */

    public void disconnect()  {
        if (!Objects.isNull(this.solrClient)) {
            try {
                this.solrClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Build a generic SearchResponse from the Solr response
     * @param searchParameters
     * @return
     * @throws IOException
     * @throws SolrServerException
     */

    public SearchResponse query(SearchParameters searchParameters)  {
        SolrQuery solrQuery = this.buildSearchQuery(searchParameters);
        try {
            QueryResponse queryResponse = this.getSolrClient().query(solrQuery);
            return this.buildQueryResponseConverterInstance().convert(queryResponse);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return new SearchResponse();
    }

    /**
     * Load a generic LoadDocument object into the SOLR instance.
     * @param loadDocument
     * @return LoadResponse
     */

    public LoadResponse load(LoadDocument loadDocument) {
        LoadResponse loadResponse = new LoadResponse().setLoadStatistics(loadDocument);
        SolrInputDocument solrInputDocument = this.buildLoadDocumentConverterInstance().convert(loadDocument);
        try {
            this.getSolrClient().add(solrInputDocument);
            this.getSolrClient().commit();
        } catch (SolrServerException | IOException e) {
            loadResponse.setErrorMessage(e.getMessage());
        }
        return loadResponse;
    }

    public SolrClient getSolrClient() { return this.solrClient; }

    public String getServerUrl() {
        return SERVER_URL;
    }

    public String getFullUrl(String core) {
        return String.format("%s/%s", this.getServerUrl(), core);
    }

    public SolrClient buildSolrClient(String coreUrl) {
        return new HttpSolrClient(coreUrl);
    }

    public QueryResponseToSearchResponse buildQueryResponseConverterInstance() {
        return new QueryResponseToSearchResponse();
    }

    public LoadDocumentToSolrInputDocument buildLoadDocumentConverterInstance() {
        return new LoadDocumentToSolrInputDocument();
    }

}
