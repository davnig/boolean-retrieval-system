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
    void given_postingList_when_toString_should_correctlyEncode() {
        String expectedEncoding = "1[1,2,3]2[1,2,3]3[1]";
        PositionalPostingList postingList = new PositionalPostingList();
        postingList.addPosting(1, 1, 2, 3);
        postingList.addPosting(2, 1);
        postingList.addPosting(2, 2);
        postingList.addPosting(2, 3);
        postingList.addPosting(3, 1);
        assertEquals(expectedEncoding, postingList.toString());
    }

    @Test
    void given_posting_when_toString_should_correctlyEncode() {
        String expectedEncoding = "1[1,2,3]";
        PositionalPosting posting = new PositionalPosting(1, 1, 2, 3);
        assertEquals(expectedEncoding, posting.toString());
    }


}
