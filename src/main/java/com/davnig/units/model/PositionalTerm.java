package com.davnig.units.model;

public class PositionalTerm extends Term<PositionalPostingList>
        implements Comparable<PositionalTerm> {

    @Override
    public int compareTo(PositionalTerm term) {
        return word.compareTo(term.word);
    }

}
