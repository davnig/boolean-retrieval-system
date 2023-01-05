package com.davnig.units.model;

import com.davnig.units.CorpusReader;
import com.davnig.units.MovieCorpusReader;
import com.davnig.units.encoding.PositionalTermDecoder;
import com.davnig.units.model.core.Corpus;
import com.davnig.units.util.StringUtils;

import java.io.*;
import java.util.Arrays;

public class MovieInvertedIndex {

    private PositionalIndex dictionary;
    private final String INDEX_FILE_PATH = "generated/index-externalizable.txt";

    public MovieInvertedIndex() {
        dictionary = new PositionalIndex();
        loadIndexFromFileOrPopulateFromCorpus();
    }

    private void loadIndexFromFileOrPopulateFromCorpus() {
        File file = new File(INDEX_FILE_PATH);
        if (file.exists()) {
            loadIndexFromFile(file);
            return;
        }
        populateIndexFromCorpus();
        saveIndexToFile(file);
    }

    private void loadIndexFromFile(File file) {
        PositionalTermDecoder decoder = new PositionalTermDecoder();
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file))
        ) {
            reader.lines()
                    .map(decoder::decode)
                    .forEach(term -> dictionary.add(term));
        } catch (IOException e) {
            System.err.println("An error occured while reading index file: " + e.getMessage());
            System.exit(1);
        }
    }

    private void populateIndexFromCorpus() {
        Corpus<Movie> movieCorpus = loadMovieCorpus();
        populateIndex(movieCorpus);
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
                dictionary.addTerm(token, docID, position);
            }
        }
    }

    private boolean isTokenNotInBlackList(String token) {
        String[] blackList = new String[]{"", "-"};
        return Arrays.stream(blackList).noneMatch(el -> el.equals(token));
    }

    private void saveIndexToFile(File file) {
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        ) {
            for (PositionalTerm term : dictionary) {
                writer.write(term.toString());
                writer.newLine();
            }
        } catch (IOException ex) {
            System.err.println("Exception: " + ex.getMessage());
        }
    }


}
