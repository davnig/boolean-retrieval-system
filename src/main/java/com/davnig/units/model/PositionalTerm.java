package com.davnig.units.model;

import com.davnig.units.model.core.Term;

public class PositionalTerm extends Term<PositionalPostingList>
        implements Comparable<PositionalTerm> {

    public PositionalTerm(String word) {
        super(word);
        this.postingList = new PositionalPostingList();
    }

    public PositionalTerm(String word, PositionalPostingList postingList) {
        super(word, postingList);
    }

    @Override
    public int compareTo(PositionalTerm term) {
        return word.compareTo(term.word);
    }

}
