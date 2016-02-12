package com.razor.solrcassandra.services;

import com.razor.solrcassandra.converters.QueryResponseToSearchResponse;
import com.razor.solrcassandra.models.SearchParameters;
import com.razor.solrcassandra.models.SearchResponse;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import spark.utils.StringUtils;

import java.io.IOException;

import static com.razor.solrcassandra.utilities.ExtendedUtils.orElse;

/**
 * Created by paul.hemmings on 2/11/16.
 */

public class SolrService {

    private static String SERVER_URL = "http://localhost:8983/solr";

    /**
     * Build SolrQuery from the generic search parameters
     * Set the default values here
     * @param searchParameters
     * @return
     */

    protected SolrQuery buildSearchQuery(SearchParameters searchParameters) {
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
     * @param solrClient
     * @param searchParameters
     * @return
     * @throws IOException
     * @throws SolrServerException
     */

    public SearchResponse query(SolrClient solrClient, SearchParameters searchParameters) throws IOException, SolrServerException {
        SolrQuery solrQuery = this.buildSearchQuery(searchParameters);
        org.apache.solr.client.solrj.response.QueryResponse queryResponse = solrClient.query(solrQuery);
        return this.buildQueryResponseConverterInstance().convert(queryResponse);
    }

    /**
     *
     * @param solrClient
     * @param solrDocument
     * @return
     * @throws IOException
     * @throws SolrServerException
     */

    public SolrService load(SolrClient solrClient, SolrInputDocument solrDocument) throws IOException, SolrServerException {
        solrClient.add(solrDocument);
        solrClient.commit();
        return this;
    }


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

}
