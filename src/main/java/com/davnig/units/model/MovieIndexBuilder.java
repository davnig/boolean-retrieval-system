package com.davnig.units.model;

import com.davnig.units.CorpusReader;
import com.davnig.units.MovieCorpusReader;
import com.davnig.units.model.core.Corpus;
import com.davnig.units.serializer.PositionalTermSerializer;
import com.davnig.units.util.StringUtils;

import java.io.*;
import java.util.Arrays;

public class MovieIndexBuilder {

    private static MovieIndexBuilder instance;
    private final String INDEX_FILE_PATH;
    private final PositionalIndex index;
    private final PositionalTermSerializer serializer;

    private MovieIndexBuilder(String indexFilePath) {
        INDEX_FILE_PATH = indexFilePath;
        index = new PositionalIndex();
        serializer = new PositionalTermSerializer();
    }

    private static MovieIndexBuilder getInstance(String indexFilePath) {
        if (instance == null) {
            instance = new MovieIndexBuilder(indexFilePath);
        }
        return instance;
    }

    public static PositionalIndex build(String source) {
        instance = MovieIndexBuilder.getInstance(source);
        return instance.loadIndexFromFileOrPopulateFromCorpus();
    }

    private PositionalIndex loadIndexFromFileOrPopulateFromCorpus() {
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
        System.out.println("Index found. Start loading in memory.");
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file))
        ) {
            reader.lines().parallel()
                    .map(serializer::deserialize)
                    .forEach(index::add);
            System.out.println("Index loaded.");
        } catch (IOException e) {
            System.err.println("An error occured while reading index file: " + e.getMessage());
            System.exit(1);
        }
    }

    private void populateIndexFromCorpus() {
        System.out.println("Index not found. Start populating from corpus.");
        Corpus<Movie> movieCorpus = loadMovieCorpus();
        populateIndex(movieCorpus);
        System.out.println("Index populated.");
    }

    private Corpus<Movie> loadMovieCorpus() {
        CorpusReader<Movie> movieCorpusReader = new MovieCorpusReader();
        return movieCorpusReader.loadCorpus();
    }

    private void populateIndex(Corpus<Movie> movieCorpus) {
        movieCorpus.getDocumentsAsStream().forEach(document -> {
            int docID = document.docID();
            Movie movie = document.content();
            String movieDesc = StringUtils.normalize(movie.description());
            String[] tokens = StringUtils.tokenize(movieDesc);
            addNewTermForEachToken(docID, tokens);
        });
    }

    private void addNewTermForEachToken(int docID, String[] tokens) {
        for (int position = 0; position < tokens.length; position++) {
            String token = tokens[position];
            if (isTokenNotInBlackList(token)) {
                index.addTerm(token, docID, position);
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
            for (PositionalTerm term : index) {
                writer.write(serializer.serialize(term));
                writer.newLine();
            }
            System.out.println("Done saving.");
        } catch (IOException ex) {
            System.err.println("Exception: " + ex.getMessage());
        }
    }

}
