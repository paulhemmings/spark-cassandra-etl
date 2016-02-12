package com.razor.myDatastaxEtl.services;

import com.google.gson.Gson;
import com.razor.myDatastaxEtl.models.SearchRequest;
import com.razor.myDatastaxEtl.models.SearchResponse;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.common.SolrInputDocument;
import spark.Request;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by paul.hemmings on 2/11/16.
 */

public class SolrService {

    private static String SERVER_URL = "http://localhost:8983/solr";

    /**
     *
     * @param solrQuery
     * @return
     * @throws IOException
     * @throws SolrServerException
     */

    public SearchResponse query(SolrClient solrClient, SolrQuery solrQuery) throws IOException, SolrServerException {

        SearchResponse solrResponse = new SearchResponse();
        org.apache.solr.client.solrj.response.QueryResponse queryResponse = solrClient.query(solrQuery);

        solrResponse.setResults(
            queryResponse.getResults()
                .stream()
                .filter(document -> !Objects.isNull(document.getFieldValueMap()))
                .map(document -> {
                    HashMap<String, Object> map = new HashMap<>();
                    document.getFieldNames().forEach(name -> map.put(name, document.getFieldValues(name)));
                    return map;
                })
                .collect(Collectors.toList())
        );

        solrResponse.setFacets(
            queryResponse.getFacetFields()
                .stream()
                .collect(Collectors.toMap(FacetField::getName, FacetField::getValueCount))
        );

        return solrResponse;
    }

    /**
     *
     * @param solrDocument
     * @return
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

}
