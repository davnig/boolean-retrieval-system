package com.davnig.units.service;

import com.davnig.units.model.PositionalTerm;

import java.util.Optional;

public interface PositionalIndex extends InvertedIndex<PositionalTerm> {

    /**
     * Adds the given term and occurrence into the index, avoiding the creation of duplicate values.
     *
     * @param word     the word to be indexed
     * @param docID    the ID of the document where the word appears
     * @param position the position inside the document where the word appears
     * @return the added {@link PositionalTerm} or {@link Optional#empty()}
     */
    Optional<PositionalTerm> addTermAndOccurrence(String word, int docID, int position);

    /**
     * Adds the given term and its multiple occurrences inside the same document into the index, avoiding the creation
     * of duplicate values.
     *
     * @param word      the word to be indexed
     * @param docID     the ID of the document where the word appears
     * @param positions the positions inside the document where the word appears
     * @return the added {@link PositionalTerm} or {@link Optional#empty()}
     */
    Optional<PositionalTerm> addTermAndOccurrences(String word, int docID, int... positions);

    /**
     * Adds a new occurrence of the given term in its postings list.
     *
     * @param docID    the ID of the document where the term appears
     * @param position the position of the term inside the document
     */
    void addOccurrence(PositionalTerm term, int docID, int position);

    /**
     * Adds multiple new occurrences of the given term in its postings list.
     *
     * @param docID     the ID of the document where the term appears
     * @param positions the positions of the term inside the document
     */
    void addOccurrencesInDoc(PositionalTerm term, int docID, int[] positions);

}
