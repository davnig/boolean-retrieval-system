package com.davnig.units.model.core;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Corpus<D> {

    private final Map<Integer, Document<D>> documents;

    public Corpus() {
        documents = new TreeMap<>();
    }

    public void addDocument(int docID, D content) {
        Document<D> document = new Document<>(docID, content);
        documents.put(docID, document);
    }

    public Document<D> getDocumentByID(int docID) {
        return documents.get(docID);
    }

    public Stream<Document<D>> getDocumentsAsStream() {
        return documents.values().stream();
    }

}
