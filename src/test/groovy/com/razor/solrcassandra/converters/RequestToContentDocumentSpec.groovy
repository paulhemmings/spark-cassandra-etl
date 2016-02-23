package com.razor.solrcassandra.converters

import spark.Request
import spock.lang.Specification

/**
 * Created by paul.hemmings on 2/22/16.
 */

class RequestToContentDocumentSpec extends Specification {

    def "it should convert request with just list of maps to ContentDocument"() {
        given:
            def converter = Spy(RequestToContentDocument.class)
            def request = Mock(Request.class)

        when:
            def contentDocument = converter.convert(request)

        then:
            1 * request.body() >> '[{ one:"item", two:"item" }]'
            contentDocument.size() == 1
            contentDocument.get(0).keySet().size() == 2
    }
}
