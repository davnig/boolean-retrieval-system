package com.davnig.units.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A three-grams index backed up by a {@link PositionalIndex}.
 */
public class ThreeGramsPositionalIndex extends PositionalIndex {

    private final Map<String, List<PositionalTerm>> threeGramsIndex;

    public ThreeGramsPositionalIndex() {
        threeGramsIndex = new HashMap<>();
    }

    /**
     * Adds the given gram to the index if it's non-existing. Adds just the term if not present
     * in the list associated with it. Do nothing if both already exist.
     *
     * @param gram {@link String}
     * @param term {@link PositionalTerm}
     */
    public void addGram(String gram, PositionalTerm term) {
        if (existsWordWithGram(term.getWord(), gram)) {
            return;
        }
        if (existsGram(gram)) {
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

    public boolean existsGram(String gram) {
        return threeGramsIndex.containsKey(gram);
    }

    public boolean existsWordWithGram(String word, String gram) {
        if (!existsGram(gram)) {
            return false;
        }
        return threeGramsIndex.get(gram).stream()
                .map(PositionalTerm::getWord)
                .anyMatch(w -> w.equals(word));
    }

}
