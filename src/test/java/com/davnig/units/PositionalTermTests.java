package com.davnig.units;

import com.davnig.units.model.PositionalPostingList;
import com.davnig.units.model.PositionalTerm;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionalTermTests {

    @Test
    void when_toString_should_correctlyEncode() {
        String expectedEncoding = "gatto:1[1,2,3]2[1,2,3]3[1]";
        PositionalPostingList postingList = new PositionalPostingList();
        postingList.addPosting(1, 1, 2, 3);
        postingList.addPosting(2, 1, 2, 3);
        postingList.addPosting(3, 1);
        PositionalTerm term = new PositionalTerm("gatto", postingList);
        assertEquals(expectedEncoding, term.toString());
    }

}
