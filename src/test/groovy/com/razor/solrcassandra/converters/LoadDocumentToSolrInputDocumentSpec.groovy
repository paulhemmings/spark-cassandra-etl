package com.razor.solrcassandra.converters

import com.razor.solrcassandra.models.LoadDocument
import com.razor.solrcassandra.models.LoadProperties
import spock.lang.Specification

/**
 * Created by paul.hemmings on 2/13/16.
 */

class LoadDocumentToSolrInputDocumentSpec extends Specification {

    def "it should load all properties from a LoadDocument into a SolrInputDocument"() {
        given:
            def converter = new LoadDocumentToSolrInputDocument()
            def loadDocument = new LoadDocument()

            loadDocument.setValues(["value-one", "value-two", "3"])
            loadDocument.setColumns([
                new LoadProperties.ColumnProperty().setColumnName("column-one"),
                new LoadProperties.ColumnProperty().setColumnName("column-two"),
                new LoadProperties.ColumnProperty().setColumnName("column-three")
            ])

        when:
            def inputDocument = converter.convert(loadDocument)

        then:
            inputDocument.getFieldValue("column-one") == "value-one"
            inputDocument.getFieldValue("column-two") == "value-two"
            inputDocument.getFieldValue("column-three") == "3"
    }
}
