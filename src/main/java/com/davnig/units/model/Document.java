package com.davnig.units.model;

public class Document<T> {

    private int docID;
    private T content;

    public Document(int docID, T content) {
        this.docID = docID;
        this.content = content;
    }

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

}
