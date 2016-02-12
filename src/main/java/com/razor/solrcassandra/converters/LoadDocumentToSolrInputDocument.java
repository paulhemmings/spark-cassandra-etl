package com.razor.solrcassandra.converters;

import com.razor.solrcassandra.models.LoadDocument;
import org.apache.solr.common.SolrInputDocument;

/**
 * Created by paul.hemmings on 2/12/16.
 */

public class LoadDocumentToSolrInputDocument {
    public SolrInputDocument convert(LoadDocument document) {
        return new SolrInputDocument();
    }
}
