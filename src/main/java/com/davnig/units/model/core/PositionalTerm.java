package com.davnig.units.model.core;

public class PositionalTerm extends Term<PositionalPostingList>
        implements Comparable<PositionalTerm> {

    @Override
    public int compareTo(PositionalTerm term) {
        return word.compareTo(term.word);
    }

}
