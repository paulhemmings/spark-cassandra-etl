package com.razor.solrcassandra.search;

import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.load.LoadDocument;
import com.razor.solrcassandra.load.LoadResponse;

/**
 * Created by paul.hemmings on 2/15/16.
 */

public interface SearchService {
    void connect(String searchIndex);
    void disconnect();
    SearchResponse query(SearchParameters searchParameters) throws ServiceException;
    LoadResponse index(LoadDocument loadDocument) throws ServiceException;
}
