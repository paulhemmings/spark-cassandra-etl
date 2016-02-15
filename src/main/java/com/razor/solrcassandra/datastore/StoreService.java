package com.razor.solrcassandra.datastore;

import com.razor.solrcassandra.load.LoadDocument;
import com.razor.solrcassandra.load.LoadProperties;
import com.razor.solrcassandra.load.LoadResponse;

/**
 * Created by paul.hemmings on 2/15/16.
 */

public interface StoreService {
    void connect(LoadProperties loadProperties);
    void disconnect();
    LoadResponse insert(LoadDocument cql);
}
