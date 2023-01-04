package com.davnig.units.model;

import java.util.NavigableSet;
import java.util.Optional;
import java.util.TreeSet;

/**
 * A positional index that manages {@link PositionalTerm}s using a Binary Search Tree.
 */
public class PositionalIndex extends TreeSet<PositionalTerm> {

    /**
     * Searches for an existing {@link PositionalTerm} with the given {@code word}. If present, adds a new posting to
     * its posting list, avoiding any duplicate insertion. If not present, adds the new term.
     *
     * @param word     the word of the {@link PositionalTerm} to be added
     * @param docID    the document ID of the {@link PositionalTerm} to be added
     * @param position the position inside the document of the {@link PositionalTerm} to be added
     */
    public void addTerm(String word, int docID, int position) {
        PositionalTerm newTerm = new PositionalTerm(word, docID, position);
        Optional<PositionalTerm> queriedTerm = findByWord(newTerm.getWord());
        if (queriedTerm.isEmpty()) {
            add(newTerm);
            return;
        }
        queriedTerm.get().addPosting(docID, position);
    }

    /**
     * Searches for a {@link PositionalTerm} with the given word.
     *
     * @param word the word to be searched
     * @return the queried term or {@link Optional#empty()} if not found
     */
    public Optional<PositionalTerm> findByWord(String word) {
        PositionalTerm example = new PositionalTerm(word, null);
        NavigableSet<PositionalTerm> resultSet = subSet(example, true, example, true);
        if (resultSet.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(resultSet.first());
    }

    public boolean existsByWord(String word) {
        PositionalTerm example = new PositionalTerm(word, null);
        return contains(example);
    }

}
