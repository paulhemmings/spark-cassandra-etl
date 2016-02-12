package com.razor.solrcassandra.services

import com.razor.solrcassandra.models.LoadDocument
import com.razor.solrcassandra.models.LoadProperties
import spock.lang.Specification

class CassandraServiceSpec extends Specification {

    def "it should join columns"() {
        given:
            def cassandraService = Spy(CassandraService)

            LoadProperties.ColumnProperty columnOne = new LoadProperties.ColumnProperty();
            columnOne.setColumnName("one");

            LoadProperties.ColumnProperty columnTwo = new LoadProperties.ColumnProperty();
            columnTwo.setColumnName("two");

        expect:
            cassandraService.buildHeader([columnOne, columnTwo]) == "one,two";
    }

    def "it should join more than one column"() {
        given:
            def cassandraService = Spy(CassandraService)

            LoadProperties.ColumnProperty columnOne = new LoadProperties.ColumnProperty();
            columnOne.setColumnName("one");
            columnOne.setColumnQuoted(false);

            LoadProperties.ColumnProperty columnTwo = new LoadProperties.ColumnProperty();
            columnTwo.setColumnName("two");
            columnTwo.setColumnQuoted(true);

        expect:
            cassandraService.buildValues([columnOne, columnTwo],["one","two"]) == "one,'two'";
    }

    def "it should build a valid CQL string"() {
        given:
            def cassandraService = Spy(CassandraService)
            def columnOne = Mock(LoadProperties.ColumnProperty);
            def loadDocument = new LoadDocument().setColumns([columnOne, columnOne]).setName("table").setValues(["one", "two"]);

        when:
            columnOne.getColumnName() >> "column"
            columnOne.isColumnQuoted() >> true

        then:
            cassandraService.buildCql(loadDocument) == "INSERT INTO table (column,column) VALUES ('one','two')";
    }

}
