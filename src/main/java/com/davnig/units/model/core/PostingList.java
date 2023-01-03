package com.davnig.units.model.core;

import java.util.ArrayList;
import java.util.Optional;

public abstract class PostingList<T extends Posting> {

    protected ArrayList<T> postings;

    public PostingList() {
        postings = new ArrayList<>();
    }

    public abstract void addPosting(int docID);

    public Optional<T> findPostingByDocID(int docID) {
        return postings.stream()
                .filter(posting -> posting.docID == docID)
                .findFirst();
    }

}
