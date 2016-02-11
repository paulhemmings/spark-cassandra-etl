package com.razor.myDatastaxEtl.models;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by paul.hemmings on 2/11/16.
 */

public class SearchResponse {

    private Optional<List<Map<String, Object>>> results;
    private Optional<Map<String, Integer>> facets;

    public Optional<List<Map<String, Object>>> getResults() {
        return results;
    }

    public void setResults(List<Map<String, Object>> results) {
        this.results = Optional.of(results);
    }

    public Optional<Map<String, Integer>> getFacets() {
        return facets;
    }

    public void setFacets(Map<String, Integer> facets) {
        this.facets = Optional.of(facets);
    }
}
