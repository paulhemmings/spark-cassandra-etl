package com.razor.solrcassandra.search

import com.razor.solrcassandra.content.ContentDocument
import com.razor.solrcassandra.converters.ContentDocumentToSolrInputDocument
import com.razor.solrcassandra.converters.QueryResponseToSearchResponse
import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.response.QueryResponse
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
            def searchParameters = Mock(SearchParameters)
            def searchResponse = Mock(SearchResponse)
            def solrQuery = Mock(SolrQuery)
            def queryResponseConverter = Mock(QueryResponseToSearchResponse)

            solrService.getSolrClient() >> solrClient

        when:
            def response = solrService.query(searchParameters)

        then:
            response == searchResponse

            1 * solrService.buildSearchQuery(searchParameters) >> solrQuery
            1 * solrClient.query(solrQuery) >> queryResponse
            1 * solrService.buildQueryResponseConverterInstance() >> queryResponseConverter
            1 * queryResponseConverter.convert(queryResponse) >> searchResponse
    }

    def "it should load a document into the SOLR instance using SOLR client API"() {
        given:
            def solrService = Spy(SolrService)
            def solrClient = Mock(SolrClient)
            def solrInputDocument = Mock(SolrInputDocument)
            def contentDocument = Mock(ContentDocument)
            def loadDocumentToSolrInputDocument = Mock(ContentDocumentToSolrInputDocument)

            solrService.getSolrClient() >> solrClient

        when:
            solrService.index(contentDocument)

        then:
            1 * solrService.buildLoadDocumentConverterInstance() >> loadDocumentToSolrInputDocument
            1 * loadDocumentToSolrInputDocument.convert(contentDocument) >> [solrInputDocument]
            1 * solrClient.add(solrInputDocument)
            1 * solrClient.commit()
            noExceptionThrown()
    }

}
