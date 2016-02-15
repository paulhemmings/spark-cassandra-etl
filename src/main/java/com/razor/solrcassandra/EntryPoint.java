package com.razor.solrcassandra;

import com.razor.solrcassandra.datastore.StoreService;
import com.razor.solrcassandra.load.LoadResource;
import com.razor.solrcassandra.search.SearchResource;
import com.razor.solrcassandra.datastore.CassandraService;
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
        StoreService storeService = new CassandraService();

        // create the resources (API service endpoints)

        new LoadResource(fileService, storeService, searchService);
        new SearchResource(searchService);
    }

}
