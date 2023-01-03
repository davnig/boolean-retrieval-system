package com.davnig.units.model.core;

import java.util.ArrayList;

public abstract class PostingList<T extends Posting> {

    protected ArrayList<T> postings;

    public PostingList() {
        this.postings = new ArrayList<>();
    }

}
