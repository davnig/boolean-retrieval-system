package com.davnig.units.service;

import com.davnig.units.model.core.Term;

import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public interface InvertedIndex<T extends Term> {

    Set<Integer> getAllDocIDs();

    Iterator<T> invertedIndexIterator();

    /**
     * Searches for a term with the given word.
     *
     * @param word the word to be searched
     * @return the queried term or {@link Optional#empty()} if not found
     */
    Optional<T> findTermByWord(String word);

    /**
     * Adds the given term to the index. If a term with the same word already
     * exists, then nothing is done.
     *
     * @param term a {@link T}
     */
    void addTerm(T term);

    boolean existsTermByWord(String word);

}
