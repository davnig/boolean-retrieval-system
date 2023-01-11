package com.davnig.units;

import com.davnig.units.model.*;
import com.davnig.units.model.core.Corpus;
import com.davnig.units.model.core.Document;
import com.davnig.units.util.SetUtils;
import com.davnig.units.util.StringUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.davnig.units.util.StringUtils.*;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class MovieIRSystem implements IRSystem<Movie, ThreeGramsPositionalIndex> {

    private static MovieIRSystem instance;
    private ThreeGramsPositionalIndex index;
    private Corpus<Movie> corpus;

    private MovieIRSystem() {
        index = null;
        corpus = null;
    }

    public static MovieIRSystem getInstance() {
        if (instance == null) {
            instance = new MovieIRSystem();
        }
        return instance;
    }

    @Override
    public void start(Corpus<Movie> corpus, ThreeGramsPositionalIndex index) {
        getInstance().corpus = corpus;
        getInstance().index = index;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Ready to search. Input your query: ");
            String query = scanner.nextLine();
            if (isPhraseQuery(query)) {
                answerPhrase(query);
            } else if (isWildcardQuery(query)) {
                answerWildcard(query);
            } else {
                answer(query);
            }
        }
    }

    @Override
    public void answer(String query) {
        checkIRSystemReadiness();
        long start = System.nanoTime();
        PositionalPostingList postingListResult;
        if (isANDQuery(query)) {
            postingListResult = answerAND(query);
        } else if (isORQuery(query)) {
            postingListResult = answerOR(query);
        } else {
            postingListResult = answerNOT(query);
        }
        List<String> movieTitles = getMovieTitlesFromPostingList(postingListResult);
        long end = System.nanoTime();
        long duration = end - start;
        System.out.printf("Found %d results in %d ms:%n%s%n%n",
                movieTitles.size(), NANOSECONDS.toMillis(duration), movieTitles);
    }

    private void checkIRSystemReadiness() {
        if (corpus == null || index == null) {
            System.err.println("Corpus or index not correctly initialized. Did you forget to call start() method?");
            System.exit(1);
        }
    }

    private PositionalPostingList answerAND(String query) {
        String[] words = normalizeAndTokenize(query, " and ");
        return fetchPostingListsAndComputeIntersection(words);
    }

    private PositionalPostingList answerOR(String query) {
        String[] words = normalizeAndTokenize(query, " or ");
        return fetchPostingListsAndComputeUnion(words);
    }

    private PositionalPostingList answerNOT(String query) {
        query = query.replace("NOT ", "");
        String[] words = normalizeAndTokenize(query, " ");
        return fetchIDsOfDocNotContainingAnyOf(words);
    }

    /**
     * Searches for movie descriptions containing the given phrase and prints their titles.
     * Only phrase queries with up to two terms are supported.
     *
     * @param query a list of terms separated by whitespaces
     */
    private void answerPhrase(String query) {
        MovieIRSystem searchEngine = getInstance();
        long start = System.nanoTime();
        String[] words = normalizeAndTokenize(query, " ");
        PositionalPostingList postingListResult = searchEngine.findPhrase(words);
        List<String> movieTitles = searchEngine.getMovieTitlesFromPostingList(postingListResult);
        long end = System.nanoTime();
        long duration = end - start;
        System.out.printf("%nFound %d results in %d ms:%n%s%n%n",
                movieTitles.size(), NANOSECONDS.toMillis(duration), movieTitles);
    }

    /**
     * Searches for movie descriptions containing words that match the given wildcard query
     * and prints their titles.
     *
     * @param query a single-term wildcard query
     */
    private void answerWildcard(String query) {
        MovieIRSystem searchEngine = getInstance();
        long start = System.nanoTime();
        List<String> threeGrams = extractThreeGramsFromQuery(query);
        String[] words = searchEngine.searchWordsByThreeGramsMatchingQuery(threeGrams, query);
        System.out.printf("%nWords matched: %n%s%n", Arrays.asList(words));
        PositionalPostingList postingListResult = searchEngine.fetchPostingListsAndComputeUnion(words);
        List<String> movieTitles = searchEngine.getMovieTitlesFromPostingList(postingListResult);
        long end = System.nanoTime();
        long duration = end - start;
        System.out.printf("Found %d results in %d ms:%n%s%n%n",
                movieTitles.size(), NANOSECONDS.toMillis(duration), movieTitles);
    }

    private String[] searchWordsByThreeGramsMatchingQuery(List<String> threeGrams, String query) {
        return threeGrams.stream()
                .map(gram -> index.findByGram(gram))
                .reduce(SetUtils::intersect).stream()
                .flatMap(Collection::stream)
                .map(PositionalTerm::getWord)
                .filter(word -> doesWordMatchWildcardQuery(word, query))
                .toArray(String[]::new);
    }

    private List<String> extractThreeGramsFromQuery(String query) {
        List<String> threeGrams = new ArrayList<>();
        query = addLeadingAndTrailingDollarSymbol(query);
        String[] tokens = query.split("\\*");
        Arrays.stream(tokens)
                .map(StringUtils::extractThreeGrams)
                .flatMap(Collection::stream)
                .forEach(threeGrams::add);
        return threeGrams;
    }

    private boolean doesWordMatchWildcardQuery(String word, String query) {
        String regex = query.replace("*", ".*");
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(word).find();
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

    private PositionalPostingList fetchIDsOfDocNotContainingAnyOf(String... words) {
        PositionalPostingList postingList = fetchPostingListsAndComputeUnion(words);
        Set<Integer> allDocIDs = index.getAllDocIDs();
        postingList.getAllDocIDs().forEach(allDocIDs::remove);
        PositionalPostingList postingListResult =
                new PositionalPostingList(allDocIDs.stream().map(PositionalPosting::new).collect(Collectors.toList()));
        return postingListResult;
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
                .map(id -> corpus.getDocumentByID(id))
                .map(Document::content)
                .map(Movie::title)
                .toList();
    }

    private boolean isPhraseQuery(String query) {
        return query.contains("\"");
    }

    private boolean isWildcardQuery(String query) {
        return query.contains("*");
    }

    private boolean isANDQuery(String query) {
        return query.split(" AND ").length > 1 || !containsBooleanOperators(query);
    }

    private boolean isORQuery(String query) {
        return containsBooleanOperators(query) && query.split(" OR ").length > 1;
    }

}
