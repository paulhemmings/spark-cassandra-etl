package com.razor.solrcassandra.converters;

import com.google.gson.Gson;
import com.razor.solrcassandra.models.SearchParameters;
import spark.Request;
import spark.utils.StringUtils;

import java.util.Objects;

import static com.razor.solrcassandra.utilities.ExtendedUtils.orElse;
import static com.razor.solrcassandra.utilities.ExtendedUtils.orEmpty;

/**
 * Created by paul.hemmings on 2/12/16.
 */

public class RequestToSearchParameters {

    /**
     * Convert the Spark HTTP request to the generic SearchParameters POJO
     * Check to see if request contains "jq" parameter as this will be a JSON object
     * If not present, convert from various request parameter values
     * @param request
     * @return
     */

    public SearchParameters convert(Request request) {
        SearchParameters searchParameters = convert(getQueryValue(request, "jq"));
        if (Objects.isNull(searchParameters)) {
            searchParameters = new SearchParameters()
                .setQuery(this.getQueryValue(request, "q"))
                .setFilterQueries(orEmpty(this.getQueryValue(request, "fq")).split(","))
                .setEndDate(this.getQueryValue(request, "endDate"))
                .setFacetLimit(this.getQueryValue(request, "facetLimit"))
                .setFacets(orEmpty(this.getQueryValue(request, "facets")).split(","))
                .setOrder(this.getQueryValue(request, "order", "ASC"))
                .setRows(this.getQueryValue(request, "rows"))
                .setSort(this.getQueryValue(request, "sort"))
                .setStartDate(this.getQueryValue(request, "start"))
                .setEndDate(this.getQueryValue(request, "end"));
        }
        return searchParameters;
    }

    public SearchParameters convert(String json) {
        return StringUtils.isEmpty(json) ? null : new Gson().fromJson(json, SearchParameters.class);
    }

    protected String getQueryValue(Request request, String queryKey) {
        return request.queryMap(queryKey).value();
    }

    protected String getQueryValue(Request request, String queryKey, String alternative) {
        return orElse(request.queryMap(queryKey).value(), alternative);
    }




}
