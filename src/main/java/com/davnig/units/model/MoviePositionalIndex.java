package com.davnig.units.model;

import java.util.Arrays;

public class MoviePositionalIndex extends BSTPositionalIndex<Movie> {

    BinarySearchTree<PositionalTerm> tree;

    public MoviePositionalIndex(Corpus<Movie> corpus) {
        super(corpus);
    }

    @Override
    void createIndexFromCorpus(Corpus<Movie> corpus) {
        corpus.getDocumentsAsStream().forEach(document -> {
            int docID = document.docID();
            Movie content = document.content();
            String movieDesc = content.description();
            if (movieDesc != null) {
                movieDesc = movieDesc.replaceAll("[^\\w^\\s^-]", movieDesc);
                String[] tokens = movieDesc.split(" ");
                System.out.println(Arrays.toString(tokens));
            }
        });
    }
}
