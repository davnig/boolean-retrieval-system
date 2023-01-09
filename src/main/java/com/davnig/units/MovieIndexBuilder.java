package com.davnig.units;

import com.davnig.units.model.Movie;
import com.davnig.units.model.PositionalTerm;
import com.davnig.units.model.ThreeGramsPositionalIndex;
import com.davnig.units.model.core.Corpus;
import com.davnig.units.serializer.PositionalTermSerializer;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

import static com.davnig.units.util.StringUtils.normalizeAndTokenize;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class MovieIndexBuilder {

    private static MovieIndexBuilder instance;
    private final String INDEX_FILE_PATH;
    private final ThreeGramsPositionalIndex index;
    private final PositionalTermSerializer serializer;

    private MovieIndexBuilder(String indexFilePath) {
        INDEX_FILE_PATH = indexFilePath;
        index = new ThreeGramsPositionalIndex();
        serializer = new PositionalTermSerializer();
    }

    private static MovieIndexBuilder getInstance(String indexFilePath) {
        if (instance == null) {
            instance = new MovieIndexBuilder(indexFilePath);
        }
        return instance;
    }

    public static ThreeGramsPositionalIndex build(String source) {
        instance = MovieIndexBuilder.getInstance(source);
        return instance.loadIndexFromFileOrPopulateFromCorpus();
    }

    private ThreeGramsPositionalIndex loadIndexFromFileOrPopulateFromCorpus() {
        File file = new File(INDEX_FILE_PATH);
        if (file.exists()) {
            loadIndexFromFile(file);
            return index;
        }
        populateIndexFromCorpus();
        saveIndexToFile(file);
        return index;
    }

    private void loadIndexFromFile(File file) {
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
            System.out.printf("Index loaded. Execution time: %d sec%n", NANOSECONDS.toSeconds(execution));
        } catch (IOException e) {
            System.err.println("An error occurred while reading index file: " + e.getMessage());
            System.exit(1);
        }
    }

    private void populateIndexFromCorpus() {
        System.out.println("Index not found. Populating from corpus...");
        Corpus<Movie> movieCorpus = loadMovieCorpus();
        populateIndex(movieCorpus);
        System.out.println("Index populated.");
    }

    private Corpus<Movie> loadMovieCorpus() {
        CorpusReader<Movie> movieCorpusReader = MovieCorpusReader.getInstance();
        return movieCorpusReader.loadCorpus();
    }

    private void populateIndex(Corpus<Movie> movieCorpus) {
        movieCorpus.getDocumentsAsStream().forEach(document -> {
            int docID = document.docID();
            Movie movie = document.content();
            String[] tokens = normalizeAndTokenize(movie.description(), " ");
            addTermOccurrenceAndGramsForEachToken(docID, tokens);
        });
    }

    private void addTermOccurrenceAndGramsForEachToken(int docID, String[] tokens) {
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

    private void saveIndexToFile(File file) {
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
