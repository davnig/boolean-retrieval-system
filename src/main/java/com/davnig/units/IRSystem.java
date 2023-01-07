package com.davnig.units;

import com.davnig.units.model.Movie;
import com.davnig.units.model.PositionalIndex;
import com.davnig.units.model.PositionalPostingList;
import com.davnig.units.model.PositionalTerm;
import com.davnig.units.model.core.Corpus;
import com.davnig.units.model.core.Document;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class IRSystem {

    private static IRSystem instance;
    private PositionalIndex index;
    private Corpus<Movie> corpus;

    private IRSystem() {
        this.index = null;
        this.corpus = null;
    }

    private static IRSystem getInstance() {
        if (instance == null) {
            instance = new IRSystem();
        }
        return instance;
    }

    public static void init(Corpus<Movie> corpus, PositionalIndex index) {
        getInstance().corpus = corpus;
        getInstance().index = index;
    }

    public static void answerAND(String query) {
        IRSystem searchEngine = getInstance();
        String[] words = query.split(" ");
        List<String> result = searchEngine.applyAND(words);
        System.out.println(result);
    }

    public static void answerOR(String query) {
        IRSystem searchEngine = getInstance();
        String[] words = query.split(" ");
        List<String> result = searchEngine.applyOR(words);
        System.out.println(result);
    }

    private List<String> applyAND(String... words) {
        PositionalPostingList intersectedPostingList = Arrays.stream(words)
                .map(index::findByWord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PositionalTerm::getPostingList)
                .reduce(PositionalPostingList::intersection)
                .orElse(new PositionalPostingList());
        return intersectedPostingList.getAllDocIDs().stream()
                .map(id -> corpus.getDocumentByID(id))
                .map(Document::content)
                .map(Movie::title)
                .toList();
    }

    private List<String> applyOR(String... words) {
        PositionalPostingList intersectedPostingList = Arrays.stream(words)
                .map(index::findByWord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PositionalTerm::getPostingList)
                .reduce(PositionalPostingList::union)
                .orElse(new PositionalPostingList());
        return intersectedPostingList.getAllDocIDs().stream()
                .map(id -> corpus.getDocumentByID(id))
                .map(Document::content)
                .map(Movie::title)
                .toList();
    }

}