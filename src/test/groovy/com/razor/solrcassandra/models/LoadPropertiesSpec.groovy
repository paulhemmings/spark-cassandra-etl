package com.razor.solrcassandra.models

import com.razor.solrcassandra.content.ContentLoadRequest
import spock.lang.Specification

/**
 * Created by paulhemmings on 10/28/15.
 */

class LoadPropertiesSpec extends Specification {

    def "it should store all content load values"() {
        given:
            def loadRequest = Spy(ContentLoadRequest)

        when:
            loadRequest.setCsvFileName("filename")
            loadRequest.setKeySpace("keyspace")
            loadRequest.setTableName("tablename")

        then:
            loadRequest.getCsvFileName() == "filename"
            loadRequest.getKeySpace() == "keyspace"
            loadRequest.getTableName() == "tablename"
    }
}
