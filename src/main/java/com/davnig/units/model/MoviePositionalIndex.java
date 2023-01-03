package com.davnig.units.model;

import com.davnig.units.model.core.Corpus;
import com.davnig.units.util.StringUtils;

import java.util.Arrays;

public class MoviePositionalIndex extends PositionalIndex<Movie> {

    public MoviePositionalIndex(Corpus<Movie> corpus) {
        super(corpus);
    }

    @Override
    void populateIndexFromCorpus(Corpus<Movie> corpus) {
        corpus.getDocumentsAsStream().forEach(document -> {
            int docID = document.docID();
            Movie movie = document.content();
            String movieDesc = StringUtils.normalize(movie.description());
            String[] tokens = StringUtils.tokenize(movieDesc);
            for (String token : tokens) {
                if (existsByWord(token)) {
                    System.out.println();
                }
            }
            System.out.println(Arrays.toString(tokens));
        });
    }

}
