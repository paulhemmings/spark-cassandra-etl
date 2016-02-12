package com.razor.solrcassandra.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileService {

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
