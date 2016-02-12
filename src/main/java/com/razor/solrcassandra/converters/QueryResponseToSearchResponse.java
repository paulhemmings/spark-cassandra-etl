package com.razor.solrcassandra.converters;

import com.razor.solrcassandra.models.SearchResponse;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * Created by paul.hemmings on 2/12/16.
 */

public class QueryResponseToSearchResponse {

    public SearchResponse convert(QueryResponse queryResponse) {
        SearchResponse response = new SearchResponse();

        response.setResults(
                queryResponse.getResults()
                    .stream()
                    .filter(document -> !Objects.isNull(document.getFieldValueMap()))
                    .map(this::convert)
                    .collect(toList())
        );

        response.setFacets(
                queryResponse.getFacetFields()
                    .stream()
                    .map(this::convert)
                    .collect(toList())
        );

        return response;
    }

    protected Map<String, Collection<Object>> convert(SolrDocument solrDocument) {
        HashMap<String, Collection<Object>> map = new HashMap<>();
        solrDocument.getFieldNames().forEach(name -> map.put(name, solrDocument.getFieldValues(name)));
        return map;
    }

    protected SearchResponse.Facet convert(FacetField facetField) {
        return new SearchResponse.Facet()
                .setFacetCount(facetField.getValueCount())
                .setName(facetField.getName())
                .setValues(facetField.getValues().stream().map(this::convert));
    }

    protected SearchResponse.Facet.FacetValue convert(FacetField.Count count) {
        return new SearchResponse.Facet.FacetValue()
                .setValue(count.getName())
                .setValueCount(count.getCount());
    }
}
