package com.razor.solrcassandra.content

import spock.lang.Specification

class CassandraServiceSpec extends Specification {

    def "it should join columns"() {
        given:
            def cassandraService = Spy(CassandraService)
            def contentDocument = new ContentDocument()
            def contentRow = contentDocument.createRow()

            contentRow.add("one", 1)
            contentRow.add("two", 2)

        expect:
            cassandraService.buildHeader(contentRow) == "\"ONE\",\"TWO\"";
    }

    def "it should join more than one column"() {
        given:
            def cassandraService = Spy(CassandraService)
            def contentDocument = new ContentDocument()
            def contentRow = contentDocument.createRow()

            contentRow.add("one", 1)
            contentRow.add("two", 2)

        expect:
            cassandraService.buildValues(contentRow) == "1,2";
    }

    def "it should build a valid CQL string"() {
        given:
            def cassandraService = Spy(CassandraService)
            def contentDocument = new ContentDocument()
            def contentRow = contentDocument.createRow()

            contentRow.add("one", 1)
            contentRow.add("two", 2)

        expect:
            cassandraService.buildCql("table", contentRow) == "INSERT INTO table (\"ONE\",\"TWO\") VALUES (1,2)";
    }

}
