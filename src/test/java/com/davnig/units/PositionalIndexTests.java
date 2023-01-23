package com.davnig.units;

import com.davnig.units.model.PositionalPostingsList;
import com.davnig.units.model.PositionalTerm;
import com.davnig.units.service.PositionalIndex;
import com.davnig.units.service.impl.BSTPositionalIndex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositionalIndexTests {

    private static PositionalIndex index;

    @BeforeAll
    static void populateIndex() {
        index = new BSTPositionalIndex();
        index.addTermAndOccurrence("a", 0, 1);
        index.addTermAndOccurrence("with", 0, 3);
        index.addTermAndOccurrence("zoo", 3, 2);
        index.addTermAndOccurrence("stars", 5, 2);
    }

    @Test
    void given_emptyIndex_when_addTerm_should_createNewEntry() {
        final String testWord = "gatto";
        BSTPositionalIndex index = new BSTPositionalIndex();
        index.addTermAndOccurrence(testWord, 1, 1);
        assertTrue(index.existsTermByWord(testWord));
        Optional<PositionalTerm> queriedTerm = index.findTermByWord(testWord);
        assertTrue(queriedTerm.isPresent());
        assertEquals(testWord, queriedTerm.get().getWord());
    }

    @Test
    void given_indexWithSameWordInDifferentDoc_when_addTerm_should_createNewPosting() {
        final String testWord = "gatto";
        index.addTermAndOccurrence(testWord, 1, 1);
        index.addTermAndOccurrence(testWord, 2, 1);
        Optional<PositionalTerm> queriedTerm = index.findTermByWord(testWord);
        assertTrue(queriedTerm.isPresent());
        assertEquals(2, queriedTerm.get().getPostingsList().size());
    }

    @Test
    void given_indexWithSameWordInSameDocInDifferentPosition_when_addTerm_should_addNewPosition() {
        final int testDocID = 1;
        final String testWord = "gatto";
        index.addTermAndOccurrence(testWord, testDocID, 1);
        index.addTermAndOccurrence(testWord, testDocID, 3);
        Optional<PositionalTerm> queriedTerm = index.findTermByWord(testWord);
        assertTrue(queriedTerm.isPresent());
        assertEquals(testWord, queriedTerm.get().getWord());
        PositionalPostingsList postingsList = queriedTerm.get().getPostingsList();
        assertEquals(1, postingsList.size());
        assertTrue(postingsList.findPostingByDocID(testDocID).isPresent());
        assertEquals(2, postingsList.findPostingByDocID(testDocID).get().size());
    }

}
