package com.davnig.units.service;

import com.davnig.units.model.core.Term;

import java.util.Set;

public interface KGramIndex<T extends Term> {

    /**
     * Retrieves the set of terms that contains the given gram.
     *
     * @param gram a k-gram
     * @return a {@link Set} of terms
     */
    Set<T> getTermsByGram(String gram);

    /**
     * Extracts and adds all the k-grams of the given term.
     *
     * @param term {@link T}
     */
    void addTerm(T term);

    /**
     * Creates a new gram, if not already existing, with the given term reference. Otherwise, adds just the term in the
     * set associated with the gram.
     *
     * @param gram {@link String}
     * @param term {@link T}
     */
    void addGramWithTerm(String gram, T term);

}
