package com.razor.solrcassandra.content;

import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.models.RequestResponse;

import java.util.List;

/**
 * Created by paul.hemmings on 2/15/16.
 */

public interface ContentService {
    void connect(ContentLoadRequest contentLoadRequest);
    void disconnect();
    RequestResponse<ContentDocument> insert(ContentDocument contentDocument) throws ServiceException;
    RequestResponse<ContentDocument> retrieve(ContentRetrieveRequest request) throws ServiceException;
}
