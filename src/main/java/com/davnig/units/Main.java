package com.davnig.units;

import com.davnig.units.model.Movie;

public class Main {

    public static void main(String[] args) {
        CorpusReader<Movie> corpusReader = new MovieCorpusReader();
        corpusReader.loadCorpus();
    }

}