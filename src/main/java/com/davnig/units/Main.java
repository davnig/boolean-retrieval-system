package com.davnig.units;

import com.davnig.units.model.Movie;
import com.davnig.units.model.ThreeGramsPositionalIndex;
import com.davnig.units.model.core.Corpus;

public class Main {

    public static void main(String[] args) {
        CorpusReader<Movie> movieCorpusReader = MovieCorpusReader.getInstance();
        Corpus<Movie> corpus = movieCorpusReader.loadCorpus();
        ThreeGramsPositionalIndex index = MovieIndexBuilder.build("data/index.txt");
        IRSystem<Movie, ThreeGramsPositionalIndex> movieSearchEngine = MovieIRSystem.getInstance();
        movieSearchEngine.start(corpus, index);
    }

}