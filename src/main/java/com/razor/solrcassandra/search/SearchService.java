package com.razor.solrcassandra.search;

import com.razor.solrcassandra.content.ContentDocument;
import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.models.RequestResponse;

/**
 * Created by paul.hemmings on 2/15/16.
 */

public interface SearchService {
    void connect(String searchIndex);
    void disconnect();
    SearchResponse query(SearchParameters searchParameters) throws ServiceException;
    RequestResponse<ContentDocument> index(ContentDocument indexDocument) throws ServiceException;
}
