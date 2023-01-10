package com.davnig.units.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class PositionalTerm implements Comparable<PositionalTerm> {

    private String word;
    private PositionalPostingsList postingsList;

    public PositionalTerm(String word) {
        this.word = word;
        this.postingsList = new PositionalPostingsList();
    }

    public PositionalTerm(String word, int docID, int position) {
        this(word);
        this.postingsList.addOccurrenceInDoc(docID, position);
    }

    public PositionalTerm(String word, int docID, int... position) {
        this(word);
        this.postingsList.addOccurrencesInDoc(docID, position);
    }

    public PositionalTerm(String word, PositionalPostingsList postingsList) {
        this.word = word;
        this.postingsList = postingsList;
    }

    /**
     * Adds a new occurrence of this term in the associated posting list.
     *
     * @param docID    the ID of the document where the term appears
     * @param position the position of the term inside the document
     */
    public void addOccurrenceInDoc(int docID, int position) {
        postingsList.addOccurrenceInDoc(docID, position);
    }

    /**
     * Adds multiple occurrences of this term in the associated posting list.
     *
     * @param docID     the ID of the document where the term appears
     * @param positions the positions of the term inside the document
     */
    public void addOccurrencesInDoc(int docID, int[] positions) {
        postingsList.addOccurrencesInDoc(docID, positions);
    }

    @Override
    public int compareTo(PositionalTerm term) {
        return word.compareTo(term.word);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionalTerm that)) return false;
        return Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", word, postingsList.toString());
    }
}
