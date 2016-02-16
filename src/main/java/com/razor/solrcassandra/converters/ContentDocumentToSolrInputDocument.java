package com.razor.solrcassandra.converters;

import com.razor.solrcassandra.content.ContentDocument;
import com.razor.solrcassandra.content.ContentLoadRequest;
import org.apache.solr.common.SolrInputDocument;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by paul.hemmings on 2/12/16.
 */

public class ContentDocumentToSolrInputDocument {

    public SolrInputDocument convert(final ContentDocument.ContentRow row) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        row.stream().forEach(cell -> solrInputDocument.setField(cell.getColumnName(), cell.getColumnValue()));
        return solrInputDocument;
    }

    public List<SolrInputDocument> convert(final ContentDocument document) {
        return document.rows().stream().map(this::convert).collect(Collectors.toList());
    }
}
