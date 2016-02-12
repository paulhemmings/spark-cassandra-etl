package com.razor.solrcassandra.models

import spock.lang.Specification

/**
 * Created by paulhemmings on 10/28/15.
 */

class LoadPropertiesSpec extends Specification {

    def "it should build a column property"() {
        given:
            def loadProperties = Spy(LoadProperties)
        when:
            loadProperties.setColumnDefinitions(["one:true", "two:false"])
            List<LoadProperties.ColumnProperty> properties = loadProperties.buildColumns()
        then:
            properties.size() == 2
            properties.get(0).getColumnName() == "one"
            properties.get(0).isColumnQuoted()

            properties.get(1).getColumnName() == "two"
            !properties.get(1).isColumnQuoted()
    }
}
