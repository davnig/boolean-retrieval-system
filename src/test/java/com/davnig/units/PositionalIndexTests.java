package com.davnig.units;

import com.davnig.units.model.PositionalIndex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PositionalIndexTests {

    @Test
    void given_emptyIndex_when_addTerm_should_createNewEntry() {
        PositionalIndex index = new PositionalIndex();
        index.addTerm("gatto", 1, 1);
        Assertions.assertTrue(index.existsByWord("gatto"));
    }

}
