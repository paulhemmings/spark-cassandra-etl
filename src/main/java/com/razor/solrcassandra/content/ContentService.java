package com.razor.solrcassandra.content;

import com.razor.solrcassandra.exceptions.ServiceException;
import com.razor.solrcassandra.models.RequestResponse;

import java.util.Map;

/**
 * Created by paul.hemmings on 2/15/16.
 */

public interface ContentService {
    RequestResponse<ContentDocument> insert(String keySpace, String tableName, ContentDocument contentDocument) throws ServiceException;
    RequestResponse<ContentDocument> retrieve(String keySpace, String tableName, Map<String, String> filters) throws ServiceException;
}
