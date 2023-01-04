package com.davnig.units;

import com.davnig.units.model.PositionalIndex;
import com.davnig.units.model.PositionalPostingList;
import com.davnig.units.model.PositionalTerm;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositionalIndexTests {

    @Test
    void given_emptyIndex_when_addTerm_should_createNewEntry() {
        PositionalIndex index = new PositionalIndex();
        index.addTerm("gatto", 1, 1);
        assertTrue(index.existsByWord("gatto"));
    }

    @Test
    void given_indexWithSameWordInDifferentDoc_when_addTerm_should_createNewPosting() {
        PositionalIndex index = new PositionalIndex();
        index.addTerm("gatto", 1, 1);
        index.addTerm("gatto", 2, 1);
        Optional<PositionalTerm> queriedTerm = index.findByWord("gatto");
        assertTrue(queriedTerm.isPresent());
        assertEquals(2, queriedTerm.get().getPostingList().size());
    }

    @Test
    void given_indexWithSameWordInSameDocInDifferentPosition_when_addTerm_should_addNewPosition() {
        final int testDocID = 1;
        final String testWord = "gatto";
        PositionalIndex index = new PositionalIndex();
        index.addTerm(testWord, testDocID, 1);
        index.addTerm("madre", testDocID, 2);
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
