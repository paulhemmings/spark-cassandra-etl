package com.razor.solrcassandra.builders;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import spark.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.razor.solrcassandra.utilities.ExtendedUtils.orElse;

/**
 * Created by paul.hemmings on 2/23/16.
 */

public class SolrQueryBuilder {

    private boolean facet = true;
    private int facetLimit = 100;
    private int rowsReturned = 40;
    private String query = "*:*";
    private List<String> filterQueries;
    private List<String> includedFacets;
    private String sort;
    private SolrQuery.ORDER order = SolrQuery.ORDER.asc;

    public SolrQueryBuilder withSort(String sort) {
        this.sort = orElse(sort, this.sort);
        return this;
    }

    public SolrQueryBuilder withFacet(boolean facet) {
        this.facet = orElse(facet, this.facet);
        return this;
    }

    public SolrQueryBuilder withFacetLimit(int facetLimit) {
        this.facetLimit = orElse(facetLimit, this.facetLimit);
        return this;
    }

    public SolrQueryBuilder withRows(int rowsReturned) {
        this.rowsReturned = orElse(rowsReturned, this.rowsReturned);
        return this;
    }

    public SolrQueryBuilder withQuery(String query) {
        this.query = orElse(query, this.query);
        return this;
    }

    public SolrQueryBuilder withFilterQueries(List<String> queries) {
        if (!CollectionUtils.isEmpty(queries) && Objects.isNull(this.filterQueries)) {
            this.filterQueries = new ArrayList<>();
        }
        queries.forEach(fq -> this.filterQueries.add(fq));
        return this;
    }

    public SolrQueryBuilder withIncludedFacets(List<String> facetList) {
        if (!CollectionUtils.isEmpty(facetList) && Objects.isNull(this.includedFacets)) {
            this.includedFacets = new ArrayList<>();
        }
        facetList.forEach(facet -> this.includedFacets.add(facet));
        return this;
    }

    public SolrQueryBuilder withOrder(SolrQuery.ORDER value) {
        this.order = value;
        return this;
    }

    public SolrQuery build() {
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setFacet(this.facet);
        solrQuery.setFacetLimit(this.facetLimit);
        solrQuery.setRows(this.rowsReturned);
        solrQuery.set("q", this.query);

        if (StringUtils.isNotEmpty(this.sort)) {
            solrQuery.setSort(this.sort, this.order);
        }

        if (!CollectionUtils.isEmpty(this.filterQueries)) {
            this.filterQueries.stream().forEach(solrQuery::addFilterQuery);
        }

        if (!CollectionUtils.isEmpty(this.includedFacets)) {
            this.includedFacets.stream().forEach(solrQuery::addFacetField);
        }

        return solrQuery;
    }
}
