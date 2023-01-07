package com.davnig.units;

import com.davnig.units.model.PositionalPosting;
import com.davnig.units.model.PositionalPostingList;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositionalPostingListTests {

    @Test
    void when_addPostingWithSameDocID_should_notCreateDuplicatePosting() {
        PositionalPostingList postingList = new PositionalPostingList();
        postingList.addPosting(1, 1);
        postingList.addPosting(1, 2);
        assertEquals(1, postingList.size());
        Optional<PositionalPosting> queriedPosting = postingList.findPostingByDocID(1);
        assertTrue(queriedPosting.isPresent());
        assertEquals(2, queriedPosting.get().size());
        List<Integer> positions = queriedPosting.get().getPositions();
        assertEquals(1, positions.get(0));
        assertEquals(2, positions.get(1));
    }

    @Test
    void when_addPostingWithSameDocIDAndPosition_should_notCreateDuplicatePosition() {
        PositionalPostingList postingList = new PositionalPostingList();
        postingList.addPosting(1, 1, 2);
        postingList.addPosting(1, 2);
        assertEquals(1, postingList.size());
        Optional<PositionalPosting> queriedPosting = postingList.findPostingByDocID(1);
        assertTrue(queriedPosting.isPresent());
        assertEquals(2, queriedPosting.get().size());
        List<Integer> positions = queriedPosting.get().getPositions();
        assertEquals(1, positions.get(0));
        assertEquals(2, positions.get(1));
    }

    @Test
    void when_intersect_should_onlyIncludePostingsWithSameDocIDInBothLists() {
        PositionalPostingList postingListA = new PositionalPostingList();
        postingListA.addPosting(4, 1, 2);
        postingListA.addPosting(25, 2);
        postingListA.addPosting(31, 40, 57);
        PositionalPostingList postingListB = new PositionalPostingList();
        postingListB.addPosting(4, 1, 34);
        postingListB.addPosting(26, 2);
        postingListB.addPosting(30, 1);
        postingListB.addPosting(31, 45, 58);
        PositionalPostingList intersection = postingListA.intersect(postingListB);
        assertEquals(2, intersection.size());
        assertTrue(intersection.findPostingByDocID(4).isPresent());
        assertTrue(intersection.findPostingByDocID(31).isPresent());
    }

    @Test
    void when_union_should_includeAllPostingsFromBothListsMaintainingAscendingOrder() {
        PositionalPostingList postingListA = new PositionalPostingList();
        postingListA.addPosting(4, 1, 2);
        postingListA.addPosting(25, 2);
        postingListA.addPosting(31, 40, 57);
        PositionalPostingList postingListB = new PositionalPostingList();
        postingListB.addPosting(4, 1, 34);
        postingListB.addPosting(26, 2);
        postingListB.addPosting(30, 1);
        postingListB.addPosting(31, 45, 58);
        PositionalPostingList union = postingListA.union(postingListB);
        assertEquals(5, union.size());
        assertEquals("4[]25[]26[]30[]31[]", union.toString());
    }


}
