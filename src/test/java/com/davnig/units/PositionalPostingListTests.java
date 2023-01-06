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


}
