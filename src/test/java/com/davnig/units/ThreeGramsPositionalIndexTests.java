package com.davnig.units;

import com.davnig.units.model.PositionalTerm;
import com.davnig.units.service.impl.ThreeGramsPositionalIndex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreeGramsPositionalIndexTests {

    @Test
    void given_term_when_extractAndAddThreeGrams_should_correctlyExtractGrams() {
        ThreeGramsPositionalIndex index = new ThreeGramsPositionalIndex();
        PositionalTerm term = new PositionalTerm("gatto");
        index.extractAndAddThreeGrams(term);
        assertEquals(5, index.size());
        String[] expectedGrams = new String[]{"$ga", "gat", "att", "tto", "to$"};
        for (String gram : expectedGrams) {
            assertTrue(index.existsGramInIndex(gram));
        }
    }

    @Test
    void given_positionalTerm_when_extractAndAddThreeGrams_should_linkGramsToTerm() {
        ThreeGramsPositionalIndex index = new ThreeGramsPositionalIndex();
        PositionalTerm term = new PositionalTerm("gatto");
        index.extractAndAddThreeGrams(term);
        String[] grams = new String[]{"$ga", "gat", "att", "tto", "to$"};
        for (String gram : grams) {
            assertEquals(1, index.getTermsByGram(gram).size());
            assertTrue(index.getTermsByGram(gram).contains(term));
        }
    }

}
