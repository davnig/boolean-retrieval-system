package com.davnig.units;

import com.davnig.units.model.Movie;
import com.davnig.units.model.PositionalIndex;
import com.davnig.units.model.PositionalPostingList;
import com.davnig.units.model.PositionalTerm;
import com.davnig.units.model.core.Corpus;
import com.davnig.units.model.core.Document;

import java.util.*;

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

    public static void start(Corpus<Movie> corpus, PositionalIndex index) {
        getInstance().corpus = corpus;
        getInstance().index = index;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Ready to search. Input your query: ");
            String query = scanner.nextLine();
            if (query.contains("\"")) {
                answerPhrase(query);
            } else if (query.contains("*")) {
                answerWildcard(query);
            } else {
                answer(query);
            }
        }
    }

    /**
     * Parses the given query to an {@code AND}-query or an {@code OR}-query and performs the search of
     * movie descriptions.
     *
     * @param query a list of terms separated by {@code AND} or {@code OR}
     */
    public static void answer(String query) {
        IRSystem searchEngine = getInstance();
        PositionalPostingList postingListResult;
        if (query.split(" AND ").length > 1) {
            String[] words = normalizeAndTokenize(query, " and ");
            postingListResult = searchEngine.fetchPostingListsAndComputeIntersection(words);
        } else if (query.split(" OR ").length > 1) {
            String[] words = normalizeAndTokenize(query, " or ");
            postingListResult = searchEngine.fetchPostingListsAndComputeUnion(words);
        } else {
            query = query.replace("NOT ", "");
            String[] words = normalizeAndTokenize(query, " ");
            Set<Integer> docIDs = searchEngine.fetchIDsOfDocNotContainingAnyOf(words);
            List<String> movieTitles = searchEngine.getMovieTitlesFromDocIDs(docIDs);
            System.out.println(movieTitles);
            return;
        }
        List<String> movieTitles = searchEngine.getMovieTitlesFromPostingList(postingListResult);
        System.out.println(movieTitles);
    }

    /**
     * Searches for movie descriptions containing all the given terms and prints their titles.
     *
     * @param query a list of terms separated by whitespaces
     */
    public static void answerAND(String query) {
        IRSystem searchEngine = getInstance();
        String[] words = normalizeAndTokenize(query, " ");
        PositionalPostingList postingListResult = searchEngine.fetchPostingListsAndComputeIntersection(words);
        List<String> movieTitles = searchEngine.getMovieTitlesFromPostingList(postingListResult);
        System.out.println(movieTitles);
    }

    /**
     * Searches for movie descriptions containing at least one of the given terms and
     * prints their titles.
     *
     * @param query a list of terms separated by whitespaces
     */
    public static void answerOR(String query) {
        IRSystem searchEngine = getInstance();
        String[] words = normalizeAndTokenize(query, " ");
        PositionalPostingList postingListResult = searchEngine.fetchPostingListsAndComputeUnion(words);
        List<String> movieTitles = searchEngine.getMovieTitlesFromPostingList(postingListResult);
        System.out.println(movieTitles);
    }

    public static void answerNOT(String query) {
        IRSystem searchEngine = getInstance();
        String[] words = normalizeAndTokenize(query, " ");
        Set<Integer> docIDs = searchEngine.fetchIDsOfDocNotContainingAnyOf(words);
        List<String> movieTitles = searchEngine.getMovieTitlesFromDocIDs(docIDs);
        System.out.println(movieTitles);
    }

    /**
     * Searches for movie descriptions containing the given phrase and prints their titles.
     * Only phrase queries with up to two terms are supported.
     *
     * @param query a list of terms separated by whitespaces
     */
    public static void answerPhrase(String query) {
        IRSystem searchEngine = getInstance();
        String[] words = normalizeAndTokenize(query, " ");
        PositionalPostingList postingListResult = searchEngine.findPhrase(words);
        List<String> movieTitles = searchEngine.getMovieTitlesFromPostingList(postingListResult);
        System.out.println(movieTitles);
    }

    private static void answerWildcard(String query) {
        // TODO
    }

    private PositionalPostingList fetchPostingListsAndComputeIntersection(String... words) {
        return Arrays.stream(words)
                .map(index::findByWord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PositionalTerm::getPostingList)
                .reduce(PositionalPostingList::intersect)
                .orElse(new PositionalPostingList());
    }

    private PositionalPostingList fetchPostingListsAndComputeUnion(String... words) {
        return Arrays.stream(words)
                .map(index::findByWord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PositionalTerm::getPostingList)
                .reduce(PositionalPostingList::union)
                .orElse(new PositionalPostingList());
    }

    private Set<Integer> fetchIDsOfDocNotContainingAnyOf(String... words) {
        PositionalPostingList postingList = fetchPostingListsAndComputeUnion(words);
        Set<Integer> allDocIDs = index.getAllDocIDs();
        postingList.getAllDocIDs().forEach(allDocIDs::remove);
        return allDocIDs;
    }

    private PositionalPostingList findPhrase(String... words) {
        return Arrays.stream(words)
                .map(index::findByWord)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(PositionalTerm::getPostingList)
                .reduce(PositionalPostingList::intersectAndFillWithAdjacentPositions)
                .orElse(new PositionalPostingList());
    }

    private List<String> getMovieTitlesFromPostingList(PositionalPostingList postingList) {
        return postingList.getAllDocIDs().stream()
                .map(id -> this.corpus.getDocumentByID(id))
                .map(Document::content)
                .map(Movie::title)
                .toList();
    }

    private List<String> getMovieTitlesFromDocIDs(Set<Integer> docIDs) {
        return docIDs.stream()
                .map(id -> this.corpus.getDocumentByID(id))
                .map(Document::content)
                .map(Movie::title)
                .toList();
    }

}
