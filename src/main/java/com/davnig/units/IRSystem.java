package com.davnig.units;

import com.davnig.units.model.Movie;
import com.davnig.units.model.PositionalIndex;
import com.davnig.units.model.PositionalPostingList;
import com.davnig.units.model.PositionalTerm;
import com.davnig.units.model.core.Corpus;
import com.davnig.units.model.core.Document;
import com.davnig.units.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.davnig.units.util.StringUtils.normalizeAndTokenize;

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

    /**
     * Parses the given query to an {@code AND}-query or an {@code OR}-query and performs the search of
     * movie descriptions.
     *
     * @param query a list of terms separated by {@code AND} or {@code OR}
     */
    public static void answer(String query) {
        IRSystem searchEngine = getInstance();
        List<String> result;
        if (query.split(" AND ").length > 1) {
            String[] words = normalizeAndTokenize(query, " and ");
            result = searchEngine.applyAND(words);
        } else {
            String[] words = normalizeAndTokenize(query, " or ");
            result = searchEngine.applyOR(words);
        }
        System.out.println(result);
    }

    /**
     * Searches for movie descriptions containing all the given terms and prints their titles.
     *
     * @param query a list of terms separated by whitespaces
     */
    public static void answerAND(String query) {
        IRSystem searchEngine = getInstance();
        String[] words = normalizeAndTokenize(query, " ");
        List<String> result = searchEngine.applyAND(words);
        System.out.println(result);
    }

    /**
     * Searches for movie descriptions containing at least one of the given terms and
     * prints their titles.
     *
     * @param query a list of terms separated by whitespaces
     */
    public static void answerOR(String query) {
        IRSystem searchEngine = getInstance();
        String normQuery = StringUtils.normalize(query);
        String[] words = normQuery.split(" ");
        List<String> result = searchEngine.applyOR(words);
        System.out.println(result);
    }

    /**
     * Searches for movie descriptions containing the given phrase and prints their titles.
     * Only phrase queries with up to two terms are supported.
     *
     * @param query a list of terms separated by whitespaces
     */
    public static void answerPhrase(String query) {
        IRSystem searchEngine = getInstance();
        String normQuery = StringUtils.normalize(query);
        String[] words = normQuery.split(" ");
        List<String> result = searchEngine.findPhrase(words);
        System.out.println(result);
    }

    private List<String> applyAND(String... words) {
        PositionalPostingList postingListIntersection = Arrays.stream(words)
                .map(index::findByWord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PositionalTerm::getPostingList)
                .reduce(PositionalPostingList::intersect)
                .orElse(new PositionalPostingList());
        return getMovieTitlesFromPostingList(postingListIntersection);
    }

    private List<String> applyOR(String... words) {
        PositionalPostingList postingListUnion = Arrays.stream(words)
                .map(index::findByWord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PositionalTerm::getPostingList)
                .reduce(PositionalPostingList::union)
                .orElse(new PositionalPostingList());
        return getMovieTitlesFromPostingList(postingListUnion);
    }

    private List<String> findPhrase(String... words) {
        PositionalPostingList postingListResult = Arrays.stream(words)
                .map(index::findByWord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PositionalTerm::getPostingList)
                .reduce(PositionalPostingList::intersectAndFillWithAdjacentPositions)
                .orElse(new PositionalPostingList());
        return getMovieTitlesFromPostingList(postingListResult);
    }

    private List<String> getMovieTitlesFromPostingList(PositionalPostingList postingList) {
        return postingList.getAllDocIDs().stream()
                .map(id -> corpus.getDocumentByID(id))
                .map(Document::content)
                .map(Movie::title)
                .toList();
    }

}
