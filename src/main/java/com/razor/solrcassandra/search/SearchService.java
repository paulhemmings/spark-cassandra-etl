package com.razor.solrcassandra.search;

import com.razor.solrcassandra.content.ContentDocument;
import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.models.RequestResponse;

/**
 * Created by paul.hemmings on 2/15/16.
 */

public interface SearchService {
    RequestResponse query(SearchParameters searchParameters) throws ServiceException;
    RequestResponse index(String core, ContentDocument contentDocument) throws ServiceException;
}
