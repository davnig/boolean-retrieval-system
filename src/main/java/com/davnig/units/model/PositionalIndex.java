package com.davnig.units.model;

import com.davnig.units.model.core.InvertedIndex;

import java.util.*;

/**
 * A positional index that manages {@link PositionalTerm}s using a Binary Search Tree.
 */
public class PositionalIndex extends InvertedIndex {

    protected final TreeSet<PositionalTerm> dictionary;

    public PositionalIndex() {
        dictionary = new TreeSet<>();
    }

    public Set<Integer> getAllDocIDs() {
        Set<Integer> set = new HashSet<>();
        for (Iterator<PositionalTerm> it = positionalIndexIterator(); it.hasNext(); ) {
            PositionalTerm term = it.next();
            set.addAll(term.getPostingList().getAllDocIDs());
        }
        return set;
    }

    public Iterator<PositionalTerm> positionalIndexIterator() {
        return dictionary.iterator();
    }

    /**
     * Adds the given term to the index. If a term with the same word already
     * exists, then nothing is done.
     *
     * @param term a {@link PositionalTerm}
     */
    public void addTerm(PositionalTerm term) {
        dictionary.add(term);
    }

    /**
     * Searches for an existing term in the index with the same {@code word} provided as parameter. If present,
     * traverses the associated posting list looking for a posting that matches the provided {@code docID}.
     * If found, adds the given {@code position}, avoiding duplicates. Otherwise, create a new posting.
     * If the term is not present in the index, adds the new term.
     *
     * @param word     the term's actual value
     * @param docID    the document ID of the term to be added
     * @param position the position where the term appears inside the document
     * @return
     */
    public Optional<PositionalTerm> addTermOccurrence(String word, int docID, int position) {
        PositionalTerm newTerm = new PositionalTerm(word, docID, position);
        dictionary.add(newTerm);
        Optional<PositionalTerm> queriedTerm = findByWord(newTerm.getWord());
        queriedTerm.ifPresent(term -> term.addOccurrenceInDoc(docID, position));
        return queriedTerm;
    }

    /**
     * Searches for an existing term in the index with the same {@code word} provided as parameter. If present,
     * traverses the associated posting list looking for a posting that matches the provided {@code docID}.
     * If found, adds the given {@code positions}, avoiding duplicates. Otherwise, create a new posting.
     * If the term is not present in the index, adds the new term.
     *
     * @param word      the term's actual value
     * @param docID     the document ID of the term to be added
     * @param positions the positions where the term appears inside the document
     * @return
     */
    public Optional<PositionalTerm> addTermOccurrences(String word, int docID, int... positions) {
        PositionalTerm newTerm = new PositionalTerm(word, docID, positions);
        dictionary.add(newTerm);
        Optional<PositionalTerm> queriedTerm = findByWord(newTerm.getWord());
        queriedTerm.ifPresent(positionalTerm -> positionalTerm.addOccurrencesInDoc(docID, positions));
        return queriedTerm;
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
