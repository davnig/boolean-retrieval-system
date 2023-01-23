package com.davnig.units;

import com.davnig.units.model.Movie;
import com.davnig.units.model.core.Corpus;
import com.davnig.units.service.CorpusReader;
import com.davnig.units.service.IRSystem;
import com.davnig.units.service.impl.MovieCorpusReader;
import com.davnig.units.service.impl.MovieIRSystem;
import com.davnig.units.service.impl.MovieIndexBuilder;
import com.davnig.units.service.impl.ThreeGramsPositionalIndex;
import org.junit.jupiter.api.BeforeAll;

public class QueryTests {

    private final String[] queries = new String[]{
            "yoda OR luke OR darth",
            "\"forrest gump\"",
    };
    private static IRSystem<Movie, ThreeGramsPositionalIndex> irSystem;

    @BeforeAll
    static void init() {
        irSystem = MovieIRSystem.getInstance();
        CorpusReader<Movie> movieCorpusReader = MovieCorpusReader.getInstance();
        Corpus<Movie> corpus = movieCorpusReader.loadCorpus();
        ThreeGramsPositionalIndex index = MovieIndexBuilder.getInstance().build();
        irSystem.start(corpus, index);
    }


    //@Test
    void given_booleanTestQuery_when_answerQuery_shouldFetchCorrectMovieTitles() {
    }

}
