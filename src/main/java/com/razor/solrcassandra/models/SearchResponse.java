package com.razor.solrcassandra.models;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by paul.hemmings on 2/11/16.
 */

public class SearchResponse {

    private Optional<List<Map<String, Collection<Object>>>> results;
    private Optional<List<Facet>> facets;

    public Optional<List<Map<String, Collection<Object>>>> getResults() {
        return results;
    }

    public SearchResponse setResults(List<Map<String, Collection<Object>>> results) {
        this.results = Optional.of(results);
        return this;
    }

    public SearchResponse addResult(Map<String, Collection<Object>> result) {
        if (this.results.isPresent()) {
            this.results.get().add(result);
        } else {
            this.results = Optional.of(Collections.singletonList(result));
        }
        return this;
    }

    public Optional<List<Facet>> getFacets() {
        return facets;
    }

    public SearchResponse setFacets(List<Facet> facets) {
        this.facets = Optional.of(facets);
        return this;
    }

    public static class Facet {
        private String name;
        private int facetCount;
        private List<FacetValue> values;

        public String getName() {
            return name;
        }

        public Facet setName(String name) {
            this.name = name;
            return this;
        }

        public int getFacetCount() {
            return facetCount;
        }

        public Facet setFacetCount(int facetCount) {
            this.facetCount = facetCount;
            return this;
        }

        public List<FacetValue> getValues() {
            return values;
        }

        public Facet setValues(List<FacetValue> values) {
            this.values = values;
            return this;
        }

        public Facet setValues(Stream<FacetValue> values) {
            this.values = values.collect(Collectors.toList());
            return this;
        }

        public static class FacetValue {
            private String value;
            private long valueCount;

            public String getValue() {
                return value;
            }

            public FacetValue setValue(String value) {
                this.value = value;
                return this;
            }

            public long getValueCount() {
                return valueCount;
            }

            public FacetValue setValueCount(long valueCount) {
                this.valueCount = valueCount;
                return this;
            }
        }
    }
}
