package com.razor.myDatastaxEtl;

import com.razor.myDatastaxEtl.resources.LoadResource;
import com.razor.myDatastaxEtl.resources.SearchResource;
import com.razor.myDatastaxEtl.services.CassandraService;
import com.razor.myDatastaxEtl.services.FileService;
import com.razor.myDatastaxEtl.services.SolrService;

import static spark.Spark.get;

public class EntryPoint {

    public static void main(String[] args) {
        new LoadResource(new FileService(), new CassandraService());
        new SearchResource(new SolrService());
    }

}
