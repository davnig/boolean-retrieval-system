package com.davnig.units;

import com.davnig.units.model.PositionalPosting;
import com.davnig.units.model.PositionalPostingList;
import com.davnig.units.model.PositionalTerm;
import com.davnig.units.serializer.PositionalPostingListSerializer;
import com.davnig.units.serializer.PositionalPostingSerializer;
import com.davnig.units.serializer.PositionalTermSerializer;
import com.davnig.units.serializer.Serializer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationTests {

    record Example<T>(
            T object,
            String string
    ) {
    }

    // SERIALIZATION

    @Test
    void given_term_when_serialize_should_correctlyEncode() {
        Serializer<PositionalTerm> serializer = new PositionalTermSerializer();
        Example<PositionalTerm> example = getTermExample();
        assertEquals(example.string, serializer.serialize(example.object));
    }

    @Test
    void given_postingList_when_serialize_should_correctlyEncode() {
        Serializer<PositionalPostingList> serializer = new PositionalPostingListSerializer();
        Example<PositionalPostingList> example = getPostingListExample();
        assertEquals(example.string, serializer.serialize(example.object));
    }

    @Test
    void given_posting_when_serialize_should_correctlyEncode() {
        Serializer<PositionalPosting> serializer = new PositionalPostingSerializer();
        Example<PositionalPosting> example = getPostingExample();
        assertEquals(example.string, serializer.serialize(example.object));
    }

    // DESERIALIZATION

    @Test
    void given_encodedPostingList_when_deserialize_should_correctlyDecode() {
        Serializer<PositionalTerm> serializer = new PositionalTermSerializer();
        Example<PositionalTerm> example = getTermExample();
        PositionalTerm result = serializer.deserialize(example.string);
        assertEquals(example.object, result);
        assertEquals(3, result.getPostingList().size());
        assertTrue(result.getPostingList().findPostingByDocID(1).isPresent());
        assertTrue(result.getPostingList().findPostingByDocID(2).isPresent());
        assertTrue(result.getPostingList().findPostingByDocID(3).isPresent());
        PositionalPosting firstPosting = result.getPostingList().findPostingByDocID(1).get();
        PositionalPosting secondPosting = result.getPostingList().findPostingByDocID(2).get();
        PositionalPosting thirdPosting = result.getPostingList().findPostingByDocID(3).get();
        assertEquals(3, firstPosting.size());
        assertEquals(3, secondPosting.size());
        assertEquals(1, thirdPosting.size());
        ArrayList<Integer> positions = new ArrayList<>();
        positions.add(1);
        positions.add(2);
        positions.add(3);
        assertTrue(firstPosting.getPositions().containsAll(positions));
        assertTrue(secondPosting.getPositions().containsAll(positions));
        assertTrue(thirdPosting.getPositions().contains(1));
    }

    @Test
    void given_encodedIndex_when_deserialize_should_correctlyDecode() {
        fail();
    }

    private Example<PositionalTerm> getTermExample() {
        return new Example<>(
                createPositionalTerm(),
                "gatto:1[1,2,3]2[1,2,3]3[1]"
        );
    }

    private Example<PositionalPostingList> getPostingListExample() {
        return new Example<>(
                createPostingList(),
                "1[1,2,3]2[1,2,3]3[1]"
        );
    }

    private Example<PositionalPosting> getPostingExample() {
        return new Example<>(
                createPosting(),
                "1[1,2,3]"
        );
    }

    private PositionalTerm createPositionalTerm() {
        PositionalPostingList postingList = new PositionalPostingList();
        postingList.addPosting(1, 1, 2, 3);
        postingList.addPosting(2, 1, 2, 3);
        postingList.addPosting(3, 1);
        return new PositionalTerm("gatto", postingList);
    }

    private PositionalPostingList createPostingList() {
        PositionalPostingList postingList = new PositionalPostingList();
        postingList.addPosting(1, 1, 2, 3);
        postingList.addPosting(2, 1);
        postingList.addPosting(2, 2);
        postingList.addPosting(2, 3);
        postingList.addPosting(3, 1);
        return postingList;
    }

    private PositionalPosting createPosting() {
        return new PositionalPosting(1, 1, 2, 3);
    }

}
