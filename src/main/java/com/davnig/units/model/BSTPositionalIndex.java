package com.davnig.units.model;

import com.davnig.units.model.core.BinarySearchTree;
import com.davnig.units.model.core.Corpus;
import com.davnig.units.model.core.PositionalTerm;

public abstract class BSTPositionalIndex<D> {

    BinarySearchTree<PositionalTerm> tree;

    public BSTPositionalIndex(Corpus<D> corpus) {
        this.tree = new BinarySearchTree<>();
        createIndexFromCorpus(corpus);
    }

    abstract void createIndexFromCorpus(Corpus<D> corpus);

}
