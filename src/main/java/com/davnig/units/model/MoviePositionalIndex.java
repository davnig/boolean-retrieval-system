package com.davnig.units.model;

import com.davnig.units.model.core.BinarySearchTree;
import com.davnig.units.model.core.Corpus;
import com.davnig.units.model.core.PositionalTerm;

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
            movieDesc = movieDesc.replaceAll("[^\\w^\\s-]", "");
            String[] tokens = movieDesc.split(" ");
            System.out.println(Arrays.toString(tokens));
        });
    }
}
