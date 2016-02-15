package com.razor.solrcassandra.search;

import com.razor.solrcassandra.exceptions.ServiceException;

/**
 * Created by paul.hemmings on 2/15/16.
 */

public class SolrClientException extends ServiceException {
    public SolrClientException(String message) {
        super(message);
    }
}
