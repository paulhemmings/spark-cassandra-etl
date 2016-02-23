package com.razor.solrcassandra.search

import com.razor.solrcassandra.content.ContentDocument
import com.razor.solrcassandra.converters.ContentDocumentToSolrInputDocument
import com.razor.solrcassandra.converters.QueryResponseToSearchResponse
import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.client.solrj.response.UpdateResponse
import org.apache.solr.common.SolrInputDocument
import spock.lang.Specification
/**
 * Created by paul.hemmings on 2/11/16.
 */

class SolrServiceSpec extends Specification {

    def "it should query data from the SOLR instance based on provided values"() {
        given:
            def core = "core"
            def solrService = Spy(SolrService)
            def solrClient = Mock(SolrClient)
            def queryResponse = Mock(QueryResponse)
            def searchParameters = Mock(SearchParameters)
            def searchResponse = Mock(SearchResponse)
            def solrQuery = Mock(SolrQuery)
            def queryResponseConverter = Mock(QueryResponseToSearchResponse)

        when:
            solrService.query(core, searchParameters)

        then:
            1 * solrService.buildClient(_ as String) >> solrClient
            1 * solrService.buildSearchQuery(searchParameters) >> solrQuery
            1 * solrClient.query(solrQuery) >> queryResponse
            1 * solrService.buildQueryResponseConverterInstance() >> queryResponseConverter
            1 * queryResponseConverter.convert(queryResponse) >> searchResponse
    }

    def "it should load a document into the SOLR instance using SOLR client API"() {
        given:
            def core = "core"
            def solrService = Spy(SolrService)
            def solrClient = Mock(SolrClient)
            def solrInputDocument = Mock(SolrInputDocument)
            def contentDocument = new ContentDocument()
            def contentRow = contentDocument.addContentRow("column", "value")
            def loadDocumentToSolrInputDocument = Mock(ContentDocumentToSolrInputDocument)
            def updateResponse = Mock(UpdateResponse)

        when:
            solrService.index(core, contentDocument)

        then:
            1 * solrService.buildClient(_ as String) >> solrClient
            1 * solrService.buildContentDocumentConverterInstance() >> loadDocumentToSolrInputDocument
            1 * loadDocumentToSolrInputDocument.convert(_) >> solrInputDocument
            1 * solrClient.add(solrInputDocument)
            1 * solrClient.commit() >> updateResponse
            noExceptionThrown()
    }

}
