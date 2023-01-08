package com.davnig.units.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A three-grams index backed up by a {@link PositionalIndex}.
 */
public class ThreeGramsPositionalIndex extends PositionalIndex {

    private final Map<String, List<PositionalTerm>> threeGramsIndex;

    public ThreeGramsPositionalIndex() {
        threeGramsIndex = new HashMap<>();
    }

    public List<PositionalTerm> findByGram(String gram) {
        return threeGramsIndex.get(gram);
    }

    @Override
    public void addTerm(String word, int docID, int... positions) {
        super.addTerm(word, docID, positions);
        findByWord(word).ifPresent(this::extractAndAddThreeGrams);
    }

    @Override
    public void addTerm(String word, int docID, int position) {
        super.addTerm(word, docID, position);
        findByWord(word).ifPresent(this::extractAndAddThreeGrams);
    }

    public void extractAndAddThreeGrams(PositionalTerm term) {
        extractThreeGrams(term.getWord()).forEach(gram -> addThreeGram(gram, term));
    }

    /**
     * Adds the given gram to the index if it's non-existing. Adds just the term if not present
     * in the list associated with it. Do nothing if both already exist.
     *
     * @param gram {@link String}
     * @param term {@link PositionalTerm}
     */
    public void addThreeGram(String gram, PositionalTerm term) {
        if (existsWordWithGramInIndex(term.getWord(), gram)) {
            return;
        }
        if (existsGramInIndex(gram)) {
            List<PositionalTerm> termList = threeGramsIndex.get(gram);
            if (termList == null) {
                termList = new ArrayList<>();
            }
            termList.add(term);
            return;
        }
        ArrayList<PositionalTerm> termList = new ArrayList<>();
        termList.add(term);
        threeGramsIndex.put(gram, termList);
    }

    private List<String> extractThreeGrams(String word) {
        String extendedWord = String.format("$%s$", word);
        Pattern pattern = Pattern.compile(".{3}");
        Matcher matcher = pattern.matcher(extendedWord);
        ArrayList<String> threeGrams = new ArrayList<>();
        for (int i = 0; i < extendedWord.length(); i++) {
            if (matcher.find(i)) {
                threeGrams.add(extendedWord.substring(matcher.start(), matcher.end()));
            }
        }
        return threeGrams;
    }

    public boolean existsGramInIndex(String gram) {
        return threeGramsIndex.containsKey(gram);
    }

    public boolean existsWordWithGramInIndex(String word, String gram) {
        if (!existsGramInIndex(gram)) {
            return false;
        }
        return threeGramsIndex.get(gram).stream()
                .map(PositionalTerm::getWord)
                .anyMatch(w -> w.equals(word));
    }

    public int size() {
        return threeGramsIndex.size();
    }

}
