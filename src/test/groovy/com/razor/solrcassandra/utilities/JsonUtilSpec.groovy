package com.razor.solrcassandra.utilities

import spock.lang.Specification

class JsonUtilSpec extends Specification {

    def ""() {
        given:
            def thing = new Thing(string: 'answer', integer: 42)
            def thingJson = "{\"string\":\"answer\",\"integer\":42}"
        when:
            def jsonUtil = new JsonUtil()
            def json1 = JsonUtil.toJson(thing)
            def json2 = JsonUtil.json().render(thing)
        then:
            json1 == thingJson
            json2 == thingJson
    }

    private static class Thing {
        String string;
        Integer integer;

        String getString() {
            return string
        }

        void setString(String string) {
            this.string = string
        }

        Integer getInteger() {
            return integer
        }

        void setInteger(Integer integer) {
            this.integer = integer
        }
    }

}
