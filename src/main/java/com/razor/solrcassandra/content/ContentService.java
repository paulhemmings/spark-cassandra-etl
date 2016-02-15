package com.razor.solrcassandra.content;

import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.load.LoadDocument;
import com.razor.solrcassandra.load.LoadProperties;
import com.razor.solrcassandra.load.LoadResponse;

/**
 * Created by paul.hemmings on 2/15/16.
 */

public interface ContentService {
    void connect(LoadProperties loadProperties);
    void disconnect();
    LoadResponse insert(LoadDocument cql) throws ServiceException;
    ContentResponse retrieve(ContentRequest request);
}
