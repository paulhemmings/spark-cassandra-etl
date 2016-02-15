package com.razor.solrcassandra.load;

import com.razor.solrcassandra.exceptions.ServiceException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileLoaderService {

    public interface FileServiceCaller {
        void handleLine(String line);
    }

    public void loadData(String fileName, FileServiceCaller fileServiceCaller) throws ServiceException {
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                for (String line; (line = br.readLine()) != null; ) {
                    if (fileServiceCaller != null) {
                        fileServiceCaller.handleLine(line);
                    }
                }
            }
        } catch(IOException io) {
            throw new ServiceException("Failed to find CSV file to index");
        }
    }
}
