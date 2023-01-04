package com.davnig.units;

import com.davnig.units.model.Movie;
import com.davnig.units.model.core.Corpus;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MovieCorpusReader implements CorpusReader<Movie> {

    private final int READING_LIMIT = 5000;
    private final String BASE_PATH = "MovieSummaries/";
    private final String TITLES_FILE_NAME = "movie.metadata.tsv";
    private final String DESC_FILE_NAME = "plot_summaries.txt";

    @Override
    public Corpus<Movie> loadCorpus() {
        Map<Integer, String> titles = loadTitles();
        Map<Integer, String> descriptions = loadDescriptions();
        return createCorpus(titles, descriptions);
    }

    private Map<Integer, String> loadTitles() {
        Map<Integer, String> titles = new HashMap<>();
        try (FileReader fileReader = new FileReader(BASE_PATH + TITLES_FILE_NAME)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                String[] tokens = line.split("\t");
                titles.put(Integer.parseInt(tokens[0]), tokens[2]);
            }
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                System.err.println("File not found");
                System.exit(1);
            }
            throw new RuntimeException(e);
        }
        return titles;
    }

    private Map<Integer, String> loadDescriptions() {
        Map<Integer, String> descriptions = new HashMap<>();
        try (FileReader fileReader = new FileReader(BASE_PATH + DESC_FILE_NAME)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                String[] tokens = line.split("\t");
                descriptions.put(Integer.parseInt(tokens[0]), tokens[1]);
            }
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                System.err.println("File not found");
                System.exit(1);
            }
            throw new RuntimeException(e);
        }
        return descriptions;
    }

    private Corpus<Movie> createCorpus(Map<Integer, String> titles, Map<Integer, String> descriptions) {
        Corpus<Movie> movieCorpus = new Corpus<>();
        int docCount = 0;
        for (Map.Entry<Integer, String> entry : titles.entrySet()) {
            if (docCount == READING_LIMIT) {
                return movieCorpus;
            }
            Integer docID = entry.getKey();
            String title = entry.getValue();
            String description = descriptions.get(docID);
            if (description != null) {
                Movie movie = new Movie(title, description);
                movieCorpus.addDocument(docCount, movie);
            }
            docCount++;
        }
        return movieCorpus;
    }

}
