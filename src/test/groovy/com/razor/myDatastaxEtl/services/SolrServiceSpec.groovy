package com.razor.myDatastaxEtl.services

import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.response.FacetField
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrDocumentList
import org.apache.solr.common.SolrInputDocument
import spock.lang.Specification
/**
 * Created by paul.hemmings on 2/11/16.
 */

class SolrServiceSpec extends Specification {

    def "it should get the root URL for the SOLR service"() {
        given:
            def solrService = Spy(SolrService)
        when:
            def url = solrService.getServerUrl()
        then:
            url == "http://localhost:8983/solr"
    }

    def "it should build the URL for the provided core"() {
        given:
            def solrService = Spy(SolrService)
            solrService.getServerUrl() >> "test-url"
        when:
            def url = solrService.getFullUrl("books-core")
        then:
            url == "test-url/books-core"
    }

    def "it should build a new instance of the SOLR client from the SOLR core URL"() {
        given:
            def solrService = Spy(SolrService)
        when:
            def solrClient = solrService.buildSolrClient("full-server-url")
        then:
            solrClient != null
    }

    def "it should query data from the SOLR instance based on provided values"() {
        given:
            def solrService = Spy(SolrService)
            def solrClient = Mock(SolrClient)
            def queryResponse = Mock(QueryResponse)
            def solrQuery = Mock(SolrQuery)
            def facetField = Mock(FacetField)

            def solrDocumentList = new SolrDocumentList()
            def solrDocument = new SolrDocument()

            solrClient.query(solrQuery) >> queryResponse
            queryResponse.getResults() >> solrDocumentList
            queryResponse.getFacetFields() >> [facetField]
            facetField.getName() >> "facet-name"
            facetField.getValueCount() >> 20

            solrDocument.addField("test", "value")
            solrDocumentList.add(solrDocument)

        when:
            def solrResponse = solrService.query(solrClient, solrQuery)

        then:
            solrResponse.getResults().isPresent()
            solrResponse.getFacets().isPresent()

            solrResponse.getFacets().get().containsKey("facet-name")
            solrResponse.getFacets().get().get("facet-name") == 20

            solrResponse.getResults().get().size() == 1
            solrResponse.getResults().get().get(0).containsKey("test")
            solrResponse.getResults().get().get(0).get("test") == ["value"]
    }

    def "it should load a document into the SOLR instance using SOLR client API"() {
        given:
            def solrService = Spy(SolrService)
            def solrClient = Mock(SolrClient)
            def solrInputDocument = Mock(SolrInputDocument)
        when:
            solrService.load(solrClient, solrInputDocument)
        then:
            1 * solrClient.add(solrInputDocument)
            1 * solrClient.commit()
            noExceptionThrown()
    }

}
