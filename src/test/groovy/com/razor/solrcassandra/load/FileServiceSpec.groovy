package com.razor.solrcassandra.load

import com.razor.solrcassandra.load.FileLoaderService
import spock.lang.Specification

class FileServiceSpec extends Specification {

    def "it should load data"() {
        given:
            FileLoaderService fileService = Spy();
            FileLoaderService.FileServiceCaller fileServiceCaller = Mock();
        when:
            fileService.loadData("filename", fileServiceCaller);
        then:
            FileNotFoundException ex = thrown()
    }
}
