package com.davnig.units.model;

import com.davnig.units.model.core.Term;
import lombok.Getter;

import java.util.Objects;

@Getter
public class PositionalTerm extends Term implements Comparable<PositionalTerm> {

    private final PositionalPostingsList postingsList;

    public PositionalTerm(String word) {
        super(word);
        this.postingsList = new PositionalPostingsList();
    }

    public PositionalTerm(String word, PositionalPostingsList postingsList) {
        super(word);
        this.postingsList = postingsList;
    }

    public PositionalTerm(String word, int docID, int position) {
        this(word);
        this.postingsList.addOccurrenceInDoc(docID, position);
    }

    public PositionalTerm(String word, int docID, int... position) {
        this(word);
        this.postingsList.addOccurrencesInDoc(docID, position);
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
