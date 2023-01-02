package com.davnig.units;

import com.davnig.units.model.Corpus;
import com.davnig.units.model.Movie;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MovieCorpusReader extends CorpusReader<Movie> {

    private final String TITLES_FILE_NAME = "movie.metadata.tsv";
    private final String DESC_FILE_NAME = "plot_summaries.txt";

    @Override
    public Corpus<Movie> loadInMemory() {
        try (FileReader fileReader = new FileReader(TITLES_FILE_NAME)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);

        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                System.err.println("File not found");
                System.exit(1);
            }
            throw new RuntimeException(e);
        }
        return null;
    }

}
