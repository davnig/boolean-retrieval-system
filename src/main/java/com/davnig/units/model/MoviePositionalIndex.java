package com.davnig.units.model;

import com.davnig.units.model.core.Corpus;
import com.davnig.units.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

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
                Optional<PositionalTerm> searchResult = findByWord(token);
                if (searchResult.isEmpty()) {
                    PositionalTerm term = new PositionalTerm(token);
                    // todo: add new term
                } else {
                    // todo: add position to existing posting list
                }
            }
            System.out.println(Arrays.toString(tokens));
        });
    }

}
