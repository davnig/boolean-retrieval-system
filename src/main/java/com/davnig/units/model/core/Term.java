package com.davnig.units.model.core;

public abstract class Term<T extends PostingList<? extends Posting>> {

    protected String word;
    protected T postingList;

    public Term(String word) {
        this.word = word;
    }

    public Term(String word, T postingList) {
        this.word = word;
        this.postingList = postingList;
    }

}
