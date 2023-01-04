package com.davnig.units;

import com.davnig.units.model.Movie;
import com.davnig.units.model.MovieInvertedIndex;
import com.davnig.units.model.core.Corpus;

public class BooleanRetrievalSystem {

    public static void main(String[] args) {
        CorpusReader<Movie> corpusReader = new MovieCorpusReader();
        Corpus<Movie> movieCorpus = corpusReader.loadCorpus();
        new MovieInvertedIndex(movieCorpus);
    }

}