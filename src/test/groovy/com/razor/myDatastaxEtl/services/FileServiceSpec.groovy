package com.razor.myDatastaxEtl.services

import org.mockito.internal.stubbing.answers.ThrowsException
import spock.lang.Specification

class FileServiceSpec extends Specification {

    def "it should load data"() {
        given:
            FileService fileService = Spy();
            FileService.FileServiceCaller fileServiceCaller = Mock();
        when:
            fileService.loadData("filename", fileServiceCaller);
        then:
            FileNotFoundException ex = thrown()
    }
}
