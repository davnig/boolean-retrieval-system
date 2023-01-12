package com.davnig.units;

import com.davnig.units.model.Movie;
import com.davnig.units.model.ThreeGramsPositionalIndex;
import com.davnig.units.model.core.Corpus;
import com.davnig.units.service.CorpusReader;
import com.davnig.units.service.IRSystem;
import com.davnig.units.service.impl.MovieCorpusReader;
import com.davnig.units.service.impl.MovieIRSystem;
import com.davnig.units.service.impl.MovieIndexBuilder;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        String query = "yoda AND luke AND darth";
        List<String> result = irSystem.answer(query);
        assertTrue(result.contains("Star Wars Episode V: The Empire Strikes Back"));
        assertTrue(result.contains("Something, Something, Something Dark Side"));
        assertTrue(result.contains("Return of the Ewok"));
        assertTrue(result.contains("Star Wars Episode III: Revenge of the Sith"));
        assertTrue(result.contains("Star Wars Episode VI: Return of the Jedi"));
        assertTrue(result.contains("It's a Trap!"));
        assertEquals(6, result.size());
    }

}
