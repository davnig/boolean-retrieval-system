package com.davnig.units.model;

import java.util.HashMap;

public class Corpus<D> {

    private HashMap<Integer, D> documents;

    public Corpus() {
        documents = new HashMap<>();
    }

    public void addDocument(int docID, D document) {
        documents.put(docID, document);
    }

    public D getDocument(int docID) {
        return documents.get(docID);
    }

}
