package com.davnig.units.model;

public class Term<T extends PostingList<? extends Posting>> {

    protected String word;
    protected T postingList;

}
