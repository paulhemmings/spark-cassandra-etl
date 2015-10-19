package com.razor.myDatastaxEtl.services

import spock.lang.Specification

class CassandraServiceSpec extends Specification {

    def "it should join columns"() {
        given:
            CassandraService cassandraService = Spy()
        expect:
            cassandraService.join(["one", "two"] as Object[], false) == "one, two";
            cassandraService.join(["one", "two"] as Object[], true) == "'one', 'two'";
    }
}
