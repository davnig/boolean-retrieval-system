package com.davnig.units;

import com.davnig.units.model.Movie;
import com.davnig.units.model.ThreeGramsPositionalIndex;
import com.davnig.units.model.core.Corpus;
import com.davnig.units.service.CorpusReader;
import com.davnig.units.service.IRSystem;
import com.davnig.units.service.impl.MovieCorpusReader;
import com.davnig.units.service.impl.MovieIRSystem;
import com.davnig.units.service.impl.MovieIndexBuilder;

public class Main {

    public static void main(String[] args) {
        CorpusReader<Movie> movieCorpusReader = MovieCorpusReader.getInstance();
        Corpus<Movie> corpus = movieCorpusReader.loadCorpus();
        ThreeGramsPositionalIndex index = MovieIndexBuilder.getInstance().build();
        IRSystem<Movie, ThreeGramsPositionalIndex> movieSearchEngine = MovieIRSystem.getInstance();
        movieSearchEngine.start(corpus, index);
    }

}