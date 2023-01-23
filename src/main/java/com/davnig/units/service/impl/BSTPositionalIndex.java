package com.davnig.units.service.impl;

import com.davnig.units.model.PositionalTerm;
import com.davnig.units.service.PositionalIndex;

import java.util.*;

/**
 * A positional index that manages terms using a Binary Search Tree.
 */
public class BSTPositionalIndex implements PositionalIndex {

    protected final TreeSet<PositionalTerm> dictionary;

    public BSTPositionalIndex() {
        dictionary = new TreeSet<>();
    }

    @Override
    public Set<Integer> getAllDocIDs() {
        Set<Integer> set = new HashSet<>();
        for (Iterator<PositionalTerm> it = invertedIndexIterator(); it.hasNext(); ) {
            PositionalTerm term = it.next();
            set.addAll(term.getPostingsList().getAllDocIDs());
        }
        return set;
    }

    @Override
    public Iterator<PositionalTerm> invertedIndexIterator() {
        return dictionary.iterator();
    }

    @Override
    public void addTerm(PositionalTerm term) {
        dictionary.add(term);
    }

    @Override
    public Optional<PositionalTerm> findTermByWord(String word) {
        PositionalTerm example = new PositionalTerm(word, null);
        NavigableSet<PositionalTerm> resultSet = dictionary.subSet(example, true, example, true);
        if (resultSet.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(resultSet.first());
    }

    @Override
    public boolean existsTermByWord(String word) {
        PositionalTerm example = new PositionalTerm(word, null);
        return dictionary.contains(example);
    }

    @Override
    public Optional<PositionalTerm> addTermAndOccurrence(String word, int docID, int position) {
        PositionalTerm newTerm = new PositionalTerm(word, docID, position);
        dictionary.add(newTerm);
        Optional<PositionalTerm> queriedTerm = findTermByWord(newTerm.getWord());
        queriedTerm.ifPresent(term -> addOccurrence(term, docID, position));
        return queriedTerm;
    }

    @Override
    public Optional<PositionalTerm> addTermAndOccurrences(String word, int docID, int... positions) {
        PositionalTerm newTerm = new PositionalTerm(word, docID, positions);
        dictionary.add(newTerm);
        Optional<PositionalTerm> queriedTerm = findTermByWord(newTerm.getWord());
        queriedTerm.ifPresent(term -> addOccurrencesInDoc(term, docID, positions));
        return queriedTerm;
    }

    @Override
    public void addOccurrence(PositionalTerm term, int docID, int position) {
        term.getPostingsList().addOccurrenceInDoc(docID, position);
    }

    @Override
    public void addOccurrencesInDoc(PositionalTerm term, int docID, int[] positions) {
        term.getPostingsList().addOccurrencesInDoc(docID, positions);
    }

}
