package com.razor.solrcassandra;

import com.razor.solrcassandra.resources.LoadResource;
import com.razor.solrcassandra.resources.SearchResource;
import com.razor.solrcassandra.services.CassandraService;
import com.razor.solrcassandra.services.FileService;
import com.razor.solrcassandra.services.SolrService;

import static spark.Spark.get;

public class EntryPoint {

    public static void main(String[] args) {
        new LoadResource(new FileService(), new CassandraService());
        new SearchResource(new SolrService());
    }

}
