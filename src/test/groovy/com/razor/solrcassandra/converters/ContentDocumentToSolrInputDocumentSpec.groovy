package com.razor.solrcassandra.converters

import com.razor.solrcassandra.content.ContentDocument
import spock.lang.Specification
/**
 * Created by paul.hemmings on 2/13/16.
 */

class ContentDocumentToSolrInputDocumentSpec extends Specification {

    def "it should load all properties from a LoadDocument into a SolrInputDocument"() {
        given:
            def converter = new ContentDocumentToSolrInputDocument()
            def contentDocument = new ContentDocument()
            def contentRow = contentDocument.createRow()

            contentRow.add("column-one", "value-one")
            contentRow.add("column-two", "value-two")
            contentRow.add("column-three", "3")

        when:
            def inputDocuments = converter.convert(contentDocument)

        then:
            inputDocuments.size() == 1
            inputDocuments.get(0).getFieldValue("column-one") == "value-one"
            inputDocuments.get(0).getFieldValue("column-two") == "value-two"
            inputDocuments.get(0).getFieldValue("column-three") == "3"
    }
}
