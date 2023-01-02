package com.davnig.units;

import com.davnig.units.model.Movie;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        CorpusReader<Movie> corpusReader = new MovieCorpusReader();
        corpusReader.loadInMemory();
    }

}