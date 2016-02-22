package com.razor.solrcassandra.converters

import spark.Request
import spock.lang.Specification

/**
 * Created by paul.hemmings on 2/22/16.
 */

class RequestToContentDocumentSpec extends Specification {

    def "it should convert request to ContentDocument"() {
        given:
            def converter = Spy(RequestToContentDocument.class)
            def request = Mock(Request.class)

        when:
            def contentDocument = converter.convert(request)

        then:
            1 * request.body() >> '{ name:"test-name", contentItems:[{ one:"item", two:"item" }]}'
            contentDocument.getName() == "test-name"
            contentDocument.rows().size() == 1
            contentDocument.rows().get(0).keySet().size() == 2
    }

    def "it should convert request with just list of maps to ContentDocument"() {
        given:
            def converter = Spy(RequestToContentDocument.class)
            def request = Mock(Request.class)

        when:
            def contentDocument = converter.convert(request)

        then:
            1 * request.body() >> '[{ one:"item", two:"item" }]'
            contentDocument.rows().size() == 1
            contentDocument.rows().get(0).keySet().size() == 2
    }
}
