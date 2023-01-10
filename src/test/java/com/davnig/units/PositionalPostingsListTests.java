package com.davnig.units;

import com.davnig.units.model.PositionalPosting;
import com.davnig.units.model.PositionalPostingsList;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositionalPostingsListTests {

    @Test
    void when_addPostingWithSameDocID_should_notCreateDuplicatePosting() {
        PositionalPostingsList postingsList = new PositionalPostingsList();
        postingsList.addOccurrenceInDoc(1, 1);
        postingsList.addOccurrenceInDoc(1, 2);
        assertEquals(1, postingsList.size());
        Optional<PositionalPosting> queriedPosting = postingsList.findPostingByDocID(1);
        assertTrue(queriedPosting.isPresent());
        assertEquals(2, queriedPosting.get().size());
        List<Integer> positions = queriedPosting.get().getPositions();
        assertEquals(1, positions.get(0));
        assertEquals(2, positions.get(1));
    }

    @Test
    void when_addPostingWithSameDocIDAndPosition_should_notCreateDuplicatePosition() {
        PositionalPostingsList postingsList = new PositionalPostingsList();
        postingsList.addOccurrencesInDoc(1, 1, 2);
        postingsList.addOccurrenceInDoc(1, 2);
        assertEquals(1, postingsList.size());
        Optional<PositionalPosting> queriedPosting = postingsList.findPostingByDocID(1);
        assertTrue(queriedPosting.isPresent());
        assertEquals(2, queriedPosting.get().size());
        List<Integer> positions = queriedPosting.get().getPositions();
        assertEquals(1, positions.get(0));
        assertEquals(2, positions.get(1));
    }

    @Test
    void when_intersect_should_onlyIncludePostingsWithSameDocIDInBothLists() {
        PositionalPostingsList postingsListA = new PositionalPostingsList();
        postingsListA.addOccurrencesInDoc(4, 1, 2);
        postingsListA.addOccurrenceInDoc(25, 2);
        postingsListA.addOccurrencesInDoc(31, 40, 57);
        PositionalPostingsList postingsListB = new PositionalPostingsList();
        postingsListB.addOccurrencesInDoc(4, 1, 34);
        postingsListB.addOccurrenceInDoc(26, 2);
        postingsListB.addOccurrenceInDoc(30, 1);
        postingsListB.addOccurrencesInDoc(31, 45, 58);
        PositionalPostingsList intersection = postingsListA.intersect(postingsListB);
        assertEquals(2, intersection.size());
        assertTrue(intersection.findPostingByDocID(4).isPresent());
        assertTrue(intersection.findPostingByDocID(31).isPresent());
    }

    @Test
    void when_union_should_includeAllPostingsFromBothListsMaintainingAscendingOrder() {
        PositionalPostingsList postingsListA = new PositionalPostingsList();
        postingsListA.addOccurrencesInDoc(4, 1, 2);
        postingsListA.addOccurrenceInDoc(25, 2);
        postingsListA.addOccurrencesInDoc(31, 40, 57);
        PositionalPostingsList postingsListB = new PositionalPostingsList();
        postingsListB.addOccurrencesInDoc(4, 1, 34);
        postingsListB.addOccurrenceInDoc(26, 2);
        postingsListB.addOccurrenceInDoc(30, 1);
        postingsListB.addOccurrencesInDoc(31, 45, 58);
        PositionalPostingsList union = postingsListA.union(postingsListB);
        assertEquals(5, union.size());
        assertEquals("4[]25[]26[]30[]31[]", union.toString());
    }


}
