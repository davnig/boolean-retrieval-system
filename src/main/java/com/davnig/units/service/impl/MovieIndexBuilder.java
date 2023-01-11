package com.davnig.units.service.impl;

import com.davnig.units.model.Movie;
import com.davnig.units.model.PositionalTerm;
import com.davnig.units.model.ThreeGramsPositionalIndex;
import com.davnig.units.model.core.Corpus;
import com.davnig.units.serializer.PositionalTermSerializer;
import com.davnig.units.service.CorpusReader;
import com.davnig.units.service.IndexBuilder;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

import static com.davnig.units.util.StringUtils.normalizeAndTokenize;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class MovieIndexBuilder implements IndexBuilder<ThreeGramsPositionalIndex> {

    private static MovieIndexBuilder instance;
    private final String INDEX_FILE_PATH = "data/index.txt";
    private final PositionalTermSerializer serializer;

    private MovieIndexBuilder() {
        serializer = new PositionalTermSerializer();
    }

    public static MovieIndexBuilder getInstance() {
        if (instance == null) {
            instance = new MovieIndexBuilder();
        }
        return instance;
    }

    @Override
    public ThreeGramsPositionalIndex build() {
        return loadIndexFromFileOrPopulateFromCorpus();
    }

    private ThreeGramsPositionalIndex loadIndexFromFileOrPopulateFromCorpus() {
        ThreeGramsPositionalIndex index = new ThreeGramsPositionalIndex();
        File file = new File(INDEX_FILE_PATH);
        if (file.exists()) {
            loadIndexFromFile(index, file);
            return index;
        }
        populateIndexFromCorpus(index);
        saveIndexToFile(index, file);
        return index;
    }

    private void loadIndexFromFile(ThreeGramsPositionalIndex index, File file) {
        System.out.println("Index found. Loading in memory...");
        long start = System.nanoTime();
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file))
        ) {
            reader.lines().parallel()
                    .map(serializer::deserialize)
                    .forEach(index::addTerm);
            long end = System.nanoTime();
            long execution = end - start;
            System.out.printf("Index loaded in %d ms%n", NANOSECONDS.toMillis(execution));
        } catch (IOException e) {
            System.err.println("An error occurred while reading index file: " + e.getMessage());
            System.exit(1);
        }
    }

    private void populateIndexFromCorpus(ThreeGramsPositionalIndex index) {
        System.out.println("Index not found. Populating from corpus...");
        long start = System.nanoTime();
        Corpus<Movie> movieCorpus = loadMovieCorpus();
        populateIndex(index, movieCorpus);
        long end = System.nanoTime();
        long duration = end - start;
        System.out.printf("Index populated in %d ms%n", NANOSECONDS.toMillis(duration));
    }

    private Corpus<Movie> loadMovieCorpus() {
        CorpusReader<Movie> movieCorpusReader = MovieCorpusReader.getInstance();
        return movieCorpusReader.loadCorpus();
    }

    private void populateIndex(ThreeGramsPositionalIndex index, Corpus<Movie> movieCorpus) {
        movieCorpus.getDocumentsAsStream().forEach(document -> {
            int docID = document.docID();
            Movie movie = document.content();
            String[] tokens = normalizeAndTokenize(movie.description(), " ");
            addTermOccurrenceAndGramsForEachToken(index, docID, tokens);
        });
    }

    private void addTermOccurrenceAndGramsForEachToken(ThreeGramsPositionalIndex index, int docID, String[] tokens) {
        for (int position = 0; position < tokens.length; position++) {
            String token = tokens[position];
            if (isTokenNotInBlackList(token)) {
                index.addTermOccurrenceAndGrams(token, docID, position);
            }
        }
    }

    private boolean isTokenNotInBlackList(String token) {
        String[] blackList = new String[]{"", "-"};
        return Arrays.stream(blackList).noneMatch(el -> el.equals(token) || token.startsWith("--"));
    }

    private void saveIndexToFile(ThreeGramsPositionalIndex index, File file) {
        System.out.println("Saving new index to file.");
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        ) {
            for (Iterator<PositionalTerm> it = index.positionalIndexIterator(); it.hasNext(); ) {
                PositionalTerm term = it.next();
                writer.write(serializer.serialize(term));
                writer.newLine();
            }
            System.out.println("Done saving.");
        } catch (IOException ex) {
            System.err.println("Exception: " + ex.getMessage());
        }
    }

}
