package com.davnig.units.model;

import java.util.Optional;

public class PositionalTerm implements Comparable<PositionalTerm> {

    private String word;
    private PositionalPostingList postingList;

    public PositionalTerm(String word) {
        this.word = word;
        this.postingList = new PositionalPostingList();
    }

    public PositionalTerm(String word, PositionalPostingList postingList) {
        this.word = word;
        this.postingList = postingList;
    }

    @Override
    public int compareTo(PositionalTerm term) {
        return word.compareTo(term.word);
    }

    public void addPosition(int docID, int position) {
        Optional<PositionalPosting> postingByDocID = postingList.findPostingByDocID(docID);
        if (postingByDocID.isPresent()) {
            postingByDocID.get().addPosition(position);
        } else {
            postingList.addPosting(docID);
            postingList.findPostingByDocID(docID).ifPresent(positionalPosting -> positionalPosting.addPosition(position));
        }
    }

    public void merge(PositionalTerm term) {
        if (word.equals(term.word)) {
            postingList.merge(term.getPostingList());
        }
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public PositionalPostingList getPostingList() {
        return postingList;
    }

    public void setPostingList(PositionalPostingList postingList) {
        this.postingList = postingList;
    }

}
