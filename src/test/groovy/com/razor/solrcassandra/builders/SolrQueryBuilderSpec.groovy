package com.razor.solrcassandra.builders

import org.apache.solr.client.solrj.SolrQuery
import spock.lang.Specification

/**
 * Created by paul.hemmings on 2/23/16.
 */
class SolrQueryBuilderSpec extends Specification {

    def "it should set all the values"() {
        given:
            def solrQueryBuilder = new SolrQueryBuilder()

        when:
            solrQueryBuilder
                    .withIncludedFacets(["one"])
                    .withFacet(false)
                    .withFacetLimit(200)
                    .withFilterQueries(["fq"])
                    .withOrder(SolrQuery.ORDER.desc)
                    .withSort("sort-column")
                    .withQuery("q:one")
                    .withRows(300)

            def solrQuery = solrQueryBuilder.build()

        then:
            solrQuery.facetFields[0] == "one"
            solrQuery.getFilterQueries()[0] == "fq"
            solrQuery.getSorts()[0].item == "sort-column"
            solrQuery.getSorts()[0].order == SolrQuery.ORDER.desc
            solrQuery.getRows() == 300
            solrQuery.getQuery() == "q:one"
            solrQuery.getFacetLimit() == 200
    }

    def "it should default all the values"() {
        given:
            def solrQueryBuilder = new SolrQueryBuilder()

        when:
            def solrQuery = solrQueryBuilder.build()

        then:
            solrQuery.facetFields == null
            solrQuery.getFilterQueries() == null
            solrQuery.getRows() == 40
            solrQuery.getQuery() == "*:*"
            solrQuery.getFacetLimit() == 100
    }
}
