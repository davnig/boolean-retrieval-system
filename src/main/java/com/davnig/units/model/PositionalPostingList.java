package com.davnig.units.model;

import java.util.ArrayList;
import java.util.Optional;

public class PositionalPostingList {

    private final ArrayList<PositionalPosting> postings;

    public PositionalPostingList() {
        postings = new ArrayList<>();
    }

    public Optional<PositionalPosting> findPostingByDocID(int docID) {
        return postings.stream()
                .filter(posting -> posting.getDocID() == docID)
                .findFirst();
    }

    public void addPosting(int docID) {
        postings.add(new PositionalPosting(docID));
    }

    public void addPosting(int docID, int position) {
        postings.add(new PositionalPosting(docID, position));
    }

    public boolean isEmpty() {
        return postings.isEmpty();
    }

    public int size() {
        return postings.size();
    }

}
