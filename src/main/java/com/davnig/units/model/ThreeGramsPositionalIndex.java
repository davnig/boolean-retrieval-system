package com.davnig.units.model;

import java.util.*;

import static com.davnig.units.util.StringUtils.extractThreeGrams;

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

    public void addTermOccurrencesAndGrams(String word, int docID, int... positions) {
        Optional<PositionalTerm> termOpt = super.addTermOccurrences(word, docID, positions);
        termOpt.ifPresent(this::extractAndAddThreeGrams);
    }

    public void addTermOccurrenceAndGrams(String word, int docID, int position) {
        Optional<PositionalTerm> termOpt = super.addTermOccurrence(word, docID, position);
        termOpt.ifPresent(this::extractAndAddThreeGrams);
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
