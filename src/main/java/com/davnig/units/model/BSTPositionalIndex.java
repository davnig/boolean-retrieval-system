package com.davnig.units.model;

public abstract class BSTPositionalIndex<D> {

    BinarySearchTree<PositionalTerm> tree;

    public BSTPositionalIndex(Corpus<D> corpus) {
        this.tree = new BinarySearchTree<>();
        createIndexFromCorpus(corpus);
    }

    abstract void createIndexFromCorpus(Corpus<D> corpus);

}
