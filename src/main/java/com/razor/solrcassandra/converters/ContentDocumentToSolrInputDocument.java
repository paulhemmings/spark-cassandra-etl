package com.razor.solrcassandra.converters;

import com.razor.solrcassandra.content.ContentDocument;
import com.razor.solrcassandra.content.ContentLoadRequest;
import org.apache.solr.common.SolrInputDocument;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by paul.hemmings on 2/12/16.
 */

public class ContentDocumentToSolrInputDocument {

    public SolrInputDocument convert(final Map<String, Object> row) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        row.keySet().forEach(key -> solrInputDocument.setField(key, row.get(key)));
        return solrInputDocument;
    }

    public SolrInputDocument convert(final ContentDocument.ContentRow row) {
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        row.keySet().forEach(key -> solrInputDocument.setField(key, row.get(key)));
        return solrInputDocument;
    }

    public List<SolrInputDocument> convert(final ContentDocument document) {
        return document.rows().stream().map(this::convert).collect(Collectors.toList());
    }
}
