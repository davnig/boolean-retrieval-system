package com.davnig.units.model;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.TreeSet;

/**
 * A positional index that manages {@link PositionalTerm}s using a Binary Search Tree.
 */
public class PositionalIndex {

    protected final TreeSet<PositionalTerm> dictionary;

    public PositionalIndex() {
        dictionary = new TreeSet<>();
    }

    public Iterator<PositionalTerm> positionalIndexIterator() {
        return dictionary.iterator();
    }

    public void add(PositionalTerm term) {
        dictionary.add(term);
    }

    /**
     * Searches for an existing {@link PositionalTerm} with the given {@code word}. If present, creates a new posting
     * with the given {@code docID} and {@code positions}, and merges it in the corresponding posting list, avoiding
     * duplicate insertion. If the term is not present, adds the new term.
     *
     * @param word      the term's actual value
     * @param docID     the document ID of the term to be added
     * @param positions the positions where the term appears inside the document
     */
    public void addTerm(String word, int docID, int... positions) {
        PositionalTerm newTerm = new PositionalTerm(word, docID, positions);
        Optional<PositionalTerm> queriedTerm = findByWord(newTerm.getWord());
        if (queriedTerm.isEmpty()) {
            dictionary.add(newTerm);
            return;
        }
        queriedTerm.get().addPosting(docID, positions);
    }

    /**
     * Searches for an existing {@link PositionalTerm} with the given {@code word}. If present, creates a new posting
     * with the given {@code docID} and {@code position}, and merges it in the corresponding posting list, avoiding
     * duplicate insertion. If not present, adds the new term.
     *
     * @param word     the term's actual value
     * @param docID    the document ID of the term to be added
     * @param position the position where the term appears inside the document
     */
    public void addTerm(String word, int docID, int position) {
        PositionalTerm newTerm = new PositionalTerm(word, docID, position);
        Optional<PositionalTerm> queriedTerm = findByWord(newTerm.getWord());
        if (queriedTerm.isEmpty()) {
            dictionary.add(newTerm);
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
        NavigableSet<PositionalTerm> resultSet = dictionary.subSet(example, true, example, true);
        if (resultSet.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(resultSet.first());
    }

    public boolean existsByWord(String word) {
        PositionalTerm example = new PositionalTerm(word, null);
        return dictionary.contains(example);
    }

}
