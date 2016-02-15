package com.razor.solrcassandra.load;

import com.datastax.driver.core.exceptions.SyntaxError;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileLoaderService {

    public interface FileServiceCaller {
        void handleLine(String line);
    }

    public void loadData(String fileName, FileServiceCaller fileServiceCaller) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            for(String line; (line = br.readLine()) != null; ) {
                if (fileServiceCaller != null) {
                    fileServiceCaller.handleLine(line);
                }
            }
        }
    }
}
