package com.razor.myDatastaxEtl.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Created by paul.hemmings on 2/11/16.
 */

public class SearchRequest extends HashMap<String, String[]> {
    public String valueList(String key) {
        return Arrays.asList(this.get(key)).stream().collect(Collectors.joining(","));
    }
}
