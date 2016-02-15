package com.razor.solrcassandra.load

import com.razor.solrcassandra.exceptions.ServiceException
import spock.lang.Specification

class FileServiceSpec extends Specification {

    def "it should load data"() {
        given:
            def fileService = Spy(FileLoaderService);
            def fileServiceCaller = Mock(FileLoaderService.FileServiceCaller);

        when:
            fileService.loadData("filename", fileServiceCaller);

        then:
            ServiceException ex = thrown()
    }
}
