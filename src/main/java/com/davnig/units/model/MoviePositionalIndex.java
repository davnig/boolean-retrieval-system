package com.davnig.units.model;

public class MoviePositionalIndex extends BSTPositionalIndex<Movie> {

    BinarySearchTree<PositionalTerm> tree;

    public MoviePositionalIndex(Corpus<Movie> corpus) {
        super(corpus);
    }

    @Override
    void createIndexFromCorpus(Corpus<Movie> corpus) {
        corpus.getDocumentsAsStream().forEach(document -> {
            int docID = document.getDocID();
            Movie content = document.getContent();
        });
    }
}
