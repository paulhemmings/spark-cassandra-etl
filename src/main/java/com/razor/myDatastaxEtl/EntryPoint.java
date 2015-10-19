package com.razor.myDatastaxEtl;

import com.razor.myDatastaxEtl.resources.LoadResource;
import com.razor.myDatastaxEtl.services.CassandraService;
import com.razor.myDatastaxEtl.services.FileService;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

public class EntryPoint {

    public static void main(String[] args) {
        // staticFileLocation("/public");
        new LoadResource(new FileService(), new CassandraService());
    }

}
