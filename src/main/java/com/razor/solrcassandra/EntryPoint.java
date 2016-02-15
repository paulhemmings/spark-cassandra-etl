package com.razor.solrcassandra;

import com.razor.solrcassandra.content.ContentService;
import com.razor.solrcassandra.load.LoadResource;
import com.razor.solrcassandra.search.SearchResource;
import com.razor.solrcassandra.content.CassandraService;
import com.razor.solrcassandra.load.FileLoaderService;
import com.razor.solrcassandra.search.SearchService;
import com.razor.solrcassandra.search.SolrService;

public class EntryPoint {

    /**
     * Entry Point
     * @param args
     */

    public static void main(String[] args) {

        // create the services

        SearchService searchService = new SolrService();
        FileLoaderService fileService = new FileLoaderService();
        ContentService contentService = new CassandraService();

        // create the resources (API service endpoints)

        new LoadResource(fileService, contentService, searchService);
        new SearchResource(searchService);
    }

}
