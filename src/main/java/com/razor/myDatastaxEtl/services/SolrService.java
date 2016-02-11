package com.razor.myDatastaxEtl.services;

import com.razor.myDatastaxEtl.models.SolrResponse;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.SolrParams;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by paul.hemmings on 2/11/16.
 */

public class SolrService {

    private static String SERVER_URL = "http://localhost:8983/solr";

    /**
     *
     * @param solrParams
     * @return
     * @throws IOException
     * @throws SolrServerException
     */

    public SolrResponse query(SolrClient solrClient, SolrParams solrParams) throws IOException, SolrServerException {

        SolrResponse solrResponse = new SolrResponse();
        QueryResponse queryResponse = solrClient.query(solrParams);

        solrResponse.setResults(
            queryResponse.getResults()
                .stream()
                .filter(document -> !Objects.isNull(document.getFieldValueMap()))
                .map(SolrDocument::getFieldValueMap)
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

    public SolrService load(SolrClient solrClient, SolrDocument solrDocument) {
        return this;
    }


    public String getServerUrl() {
        return SERVER_URL;
    }

    public String getCoreUrl(String core) {
        return String.format("%s/%s", this.getServerUrl(), core);
    }

    public SolrClient buildSolrClient(String coreUrl) {
        return new HttpSolrClient(coreUrl);
    }

}
