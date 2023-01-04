package com.davnig.units.model;

import com.davnig.units.model.core.Corpus;
import com.davnig.units.util.StringUtils;

public class MovieInvertedIndex {

    private final PositionalIndex dictionary;

    public MovieInvertedIndex(Corpus<Movie> corpus) {
        dictionary = new PositionalIndex();
        populateIndexFromCorpus(corpus);
        saveIndexToFile();
    }

    void populateIndexFromCorpus(Corpus<Movie> corpus) {
        corpus.getDocumentsAsStream().forEach(document -> {
            int docID = document.docID();
            Movie movie = document.content();
            String movieDesc = StringUtils.normalize(movie.description());
            String[] tokens = StringUtils.tokenize(movieDesc);
            for (int position = 0; position < tokens.length; position++) {
                String token = tokens[position];
                dictionary.addTerm(token, docID, position);
            }
        });
        System.out.println(dictionary.first().getWord());
    }

    private void saveIndexToFile() {
        // todo
    }


}
