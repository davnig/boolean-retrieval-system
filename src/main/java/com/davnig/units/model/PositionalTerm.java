package com.davnig.units.model;

import lombok.Getter;

import java.util.Optional;

@Getter
public class PositionalTerm implements Comparable<PositionalTerm> {

    private final String word;
    private final PositionalPostingList postingList;

    public PositionalTerm(String word) {
        this.word = word;
        this.postingList = new PositionalPostingList();
    }

    public PositionalTerm(String word, int docID, int position) {
        this(word);
        this.postingList.addPosting(docID, position);
    }

    public PositionalTerm(String word, PositionalPostingList postingList) {
        this.word = word;
        this.postingList = postingList;
    }

    /**
     * Searches for an existing {@link PositionalPosting} with the given {@code docID}. If present, add the given
     * position, avoiding any duplicate insertion. If not present, creates and add a new posting to the list with the
     * given parameters.
     *
     * @param docID    the document ID
     * @param position the term position inside the document
     */
    public void addPosting(int docID, int position) {
        Optional<PositionalPosting> queriedPosting = postingList.findPostingByDocID(docID);
        if (queriedPosting.isEmpty()) {
            postingList.addPosting(docID, position);
            return;
        }
        queriedPosting.get().addPosition(position);
    }

    @Override
    public int compareTo(PositionalTerm term) {
        return word.compareTo(term.word);
    }

}
