package com.razor.solrcassandra.converters;

import com.razor.solrcassandra.content.ContentDocument;
import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.load.FileLoaderService;
import spark.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by paul.hemmings on 2/23/16.
 */
public class CsvFileToContentDocument {

    /**
     * Loads CSV file into content document (map list)
     * Assumes first row is list of column headers
     * @param filename
     * @param fileLoaderService
     * @return
     */

    public ContentDocument convert(String filename, FileLoaderService fileLoaderService) throws ServiceException {
        final ContentDocument contentDocument = new ContentDocument();
        final List<String> headers = new ArrayList<>();
        fileLoaderService.loadData(filename, line -> {
            List<String> values = Arrays.asList(line.split(","));
            if (CollectionUtils.isEmpty(headers)) {
                values.forEach(headers::add);
            } else {
                IntStream.range(0, values.size()).forEach(index -> {
                    contentDocument.addContentRow(headers.get(index), values.get(index));
                });
            }
        });
        return contentDocument;
    }
}
