package com.davnig.units.model.core;

public abstract class Term<T extends PostingList<? extends Posting>> {

    protected String word;
    protected T postingList;

}
