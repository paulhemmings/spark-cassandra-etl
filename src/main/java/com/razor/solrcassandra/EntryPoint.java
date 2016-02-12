package com.razor.solrcassandra;

import com.razor.solrcassandra.resources.LoadResource;
import com.razor.solrcassandra.resources.SearchResource;
import com.razor.solrcassandra.services.CassandraService;
import com.razor.solrcassandra.services.FileService;
import com.razor.solrcassandra.services.SolrService;

public class EntryPoint {

    /**
     * Entry Point
     * @param args
     */

    public static void main(String[] args) {

        // create the services

        SolrService solrService = new SolrService();
        FileService fileService = new FileService();
        CassandraService cassandraService = new CassandraService();

        // create the resources (API service endpoints)

        new LoadResource(fileService, cassandraService, solrService);
        new SearchResource(solrService);
    }

}
