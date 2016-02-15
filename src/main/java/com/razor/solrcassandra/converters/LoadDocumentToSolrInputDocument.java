package com.razor.solrcassandra.converters;

import com.razor.solrcassandra.load.LoadDocument;
import com.razor.solrcassandra.load.LoadProperties;
import org.apache.solr.common.SolrInputDocument;

/**
 * Created by paul.hemmings on 2/12/16.
 */

public class LoadDocumentToSolrInputDocument {
    public SolrInputDocument convert(final LoadDocument document) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();

        int index = 0;
        for(LoadProperties.ColumnProperty columnProperty : document.getColumns()) {
            solrInputDocument.setField(columnProperty.getColumnName(), document.getValues().get(index));
            index ++;
        }

        return solrInputDocument;
    }
}
