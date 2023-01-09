package com.davnig.units.model;

import java.util.*;

import static com.davnig.units.util.StringUtils.extractThreeGrams;

/**
 * A three-grams index backed up by a {@link PositionalIndex}.
 */
public class ThreeGramsPositionalIndex extends PositionalIndex {

    private final Map<String, Set<PositionalTerm>> threeGramsIndex;

    public ThreeGramsPositionalIndex() {
        threeGramsIndex = new Hashtable<>();
    }

    public Set<PositionalTerm> findByGram(String gram) {
        return threeGramsIndex.get(gram);
    }

    @Override
    public void addTerm(PositionalTerm term) {
        super.addTerm(term);
        extractAndAddThreeGrams(term);
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
        if (existsGramInIndex(gram)) {
            Set<PositionalTerm> termSet = threeGramsIndex.get(gram);
            if (termSet == null) {
                termSet = new HashSet<>();
            } else termSet.remove(term); // the removal method uses equal to check existence, so it just compares docIDs
            termSet.add(term);
            return;
        }
        Set<PositionalTerm> termList = new HashSet<>();
        termList.add(term);
        threeGramsIndex.put(gram, termList);
    }

    public boolean existsGramInIndex(String gram) {
        return threeGramsIndex.containsKey(gram);
    }

    public int size() {
        return threeGramsIndex.size();
    }

}
