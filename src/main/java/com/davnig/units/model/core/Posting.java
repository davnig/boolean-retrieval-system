package com.davnig.units.model.core;

public abstract class Posting {

    protected int docID;

    public Posting(int docID) {
        this.docID = docID;
    }

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

}
