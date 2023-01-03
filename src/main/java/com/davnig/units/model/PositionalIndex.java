package com.davnig.units.model;

import com.davnig.units.model.core.BinarySearchTree;
import com.davnig.units.model.core.Corpus;

import java.util.Optional;

/**
 * An abstract class that models a {@link PositionalIndex} that manages documents of type {@link D}.
 *
 * @param <D> the type of the documents
 */
public abstract class PositionalIndex<D> extends BinarySearchTree<PositionalTerm> {

    public PositionalIndex(Corpus<D> corpus) {
        populateIndexFromCorpus(corpus);
    }

    abstract void populateIndexFromCorpus(Corpus<D> corpus);

    public Optional<PositionalTerm> findByWord(String word) {
        PositionalTerm example = new PositionalTerm(word, null);
        return findByExample(example);
    }

    public boolean existsByWord(String word) {
        PositionalTerm example = new PositionalTerm(word, null);
        return existsByExample(example);
    }

}
