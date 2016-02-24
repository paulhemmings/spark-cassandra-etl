package com.razor.solrcassandra.search;

import com.razor.solrcassandra.utilities.ExtendedUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by paul.hemmings on 2/12/16.
 */

public class SearchParameters {

    public static final String ASC = "ASC";

    // search query (q)
    private String query;
    private List<String> filterQueries;

    // pagination
    private String start;
    private String rows;

    // sort by asc|desc
    private String sort;
    private String order;

    // query by facets
    private List<String> facets;

    // delimit by date
    private String startDate;
    private String endDate;

    // maximum number of facets to return
    private String facetLimit;

    /** Accessors */

    public String getQuery() {
        return query;
    }

    public SearchParameters setQuery(String query) {
        this.query = query;
        return this;
    }

    public String getStart() {
        return start;
    }

    public SearchParameters setStart(String start) {
        this.start = start;
        return this;
    }

    public Integer getRows() {
        return StringUtils.isNumeric(rows) ? Integer.parseInt(rows) : null;
    }

    public SearchParameters setRows(String rows) {
        this.rows = rows;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public SearchParameters setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public SearchParameters setOrder(String order) {
        this.order = order;
        return this;
    }

    public List<String> getFacets() {
        return ExtendedUtils.orEmpty(facets);
    }

    public SearchParameters setFacets(String[] facets) {
        this.facets = Arrays.asList(facets).stream().filter(StringUtils::isNotEmpty).collect(toList());
        return this;
    }

    public String getStartDate() {
        return startDate;
    }

    public SearchParameters setStartDate(String startDate) {
        this.startDate = startDate;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public SearchParameters setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public Integer getFacetLimit() {
        return StringUtils.isNumeric(facetLimit) ? Integer.valueOf(facetLimit) : null;
    }

    public SearchParameters setFacetLimit(String facetLimit) {
        this.facetLimit = facetLimit;
        return this;
    }

    public List<String> getFilterQueries() {
        return ExtendedUtils.orEmpty(filterQueries);
    }

    public SearchParameters setFilterQueries(String[] filterQueries) {
        this.filterQueries = Arrays.asList(filterQueries).stream().filter(StringUtils::isNotEmpty).collect(toList());
        return this;
    }
}
