package com.davnig.units;

import com.davnig.units.model.PositionalIndex;
import com.davnig.units.model.PositionalPostingList;
import com.davnig.units.model.PositionalTerm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositionalIndexTests {

    private static PositionalIndex index;

    @BeforeAll
    static void populateIndex() {
        index = new PositionalIndex();
        index.addTerm("a", 0, 1);
        index.addTerm("with", 0, 3);
        index.addTerm("zoo", 3, 2);
        index.addTerm("stars", 5, 2);
    }

    @Test
    void given_emptyIndex_when_addTerm_should_createNewEntry() {
        final String testWord = "gatto";
        PositionalIndex index = new PositionalIndex();
        index.addTerm(testWord, 1, 1);
        assertTrue(index.existsByWord(testWord));
        Optional<PositionalTerm> queriedTerm = index.findByWord(testWord);
        assertTrue(queriedTerm.isPresent());
        assertEquals(testWord, queriedTerm.get().getWord());
    }

    @Test
    void given_indexWithSameWordInDifferentDoc_when_addTerm_should_createNewPosting() {
        final String testWord = "gatto";
        index.addTerm(testWord, 1, 1);
        index.addTerm(testWord, 2, 1);
        Optional<PositionalTerm> queriedTerm = index.findByWord(testWord);
        assertTrue(queriedTerm.isPresent());
        assertEquals(2, queriedTerm.get().getPostingList().size());
    }

    @Test
    void given_indexWithSameWordInSameDocInDifferentPosition_when_addTerm_should_addNewPosition() {
        final int testDocID = 1;
        final String testWord = "gatto";
        index.addTerm(testWord, testDocID, 1);
        index.addTerm(testWord, testDocID, 3);
        Optional<PositionalTerm> queriedTerm = index.findByWord(testWord);
        assertTrue(queriedTerm.isPresent());
        assertEquals(testWord, queriedTerm.get().getWord());
        PositionalPostingList postingList = queriedTerm.get().getPostingList();
        assertEquals(1, postingList.size());
        assertTrue(postingList.findPostingByDocID(testDocID).isPresent());
        assertEquals(2, postingList.findPostingByDocID(testDocID).get().size());
    }

}
