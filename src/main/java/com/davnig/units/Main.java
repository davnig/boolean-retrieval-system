package com.davnig.units;

import com.davnig.units.model.Movie;
import com.davnig.units.model.MovieIndexBuilder;
import com.davnig.units.model.PositionalIndex;
import com.davnig.units.model.core.Corpus;

public class Main {

    public static void main(String[] args) {
        CorpusReader<Movie> movieCorpusReader = MovieCorpusReader.getInstance();
        Corpus<Movie> corpus = movieCorpusReader.loadCorpus();
        PositionalIndex index = MovieIndexBuilder.build("data/index.txt");
        IRSystem.init(corpus, index);
        IRSystem.answerAND("yoda luke darth");
        IRSystem.answerOR("yoda luke darth");
        IRSystem.answerPhrase("darth vader");
    }

}