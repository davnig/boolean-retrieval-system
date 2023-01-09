package com.davnig.units;

import com.davnig.units.model.PositionalIndex;
import com.davnig.units.model.PositionalPosting;
import com.davnig.units.model.PositionalPostingList;
import com.davnig.units.model.PositionalTerm;
import com.davnig.units.serializer.PositionalPostingListSerializer;
import com.davnig.units.serializer.PositionalPostingSerializer;
import com.davnig.units.serializer.PositionalTermSerializer;
import com.davnig.units.serializer.Serializer;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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
        Example<PositionalTerm> termExample = getTermExample();
        PositionalTerm result = serializer.deserialize(termExample.string);
        assertEquals(termExample.object, result);
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
        Serializer<PositionalTerm> serializer = new PositionalTermSerializer();
        Example<PositionalIndex> indexExample = getIndexExample();
        Reader stringReader = new StringReader(indexExample.string);
        PositionalIndex result = new PositionalIndex();
        try (
                BufferedReader reader = new BufferedReader(stringReader)
        ) {
            reader.lines()
                    .map(serializer::deserialize)
                    .forEach(result::addTerm);
        } catch (IOException e) {
            fail();
        }
        result.positionalIndexIterator().forEachRemaining(System.out::println);
        assertTrue(result.existsByWord("cat"));
        assertTrue(result.existsByWord("dog"));
        PositionalTerm expectedCat = indexExample.object.findByWord("cat").get();
        PositionalTerm expectedDog = indexExample.object.findByWord("dog").get();
        PositionalTerm catResult = result.findByWord("cat").get();
        PositionalTerm dogResult = result.findByWord("dog").get();
        assertEquals(expectedCat.getPostingList().size(), catResult.getPostingList().size());
        assertEquals(expectedDog.getPostingList().size(), dogResult.getPostingList().size());
        assertTrue(catResult.getPostingList().findPostingByDocID(2).isPresent());
        assertTrue(dogResult.getPostingList().findPostingByDocID(23567).isPresent());
        assertEquals(expectedCat.getPostingList().findPostingByDocID(2).get().getPositions().get(2),
                catResult.getPostingList().findPostingByDocID(2).get().getPositions().get(2));
        assertEquals(expectedDog.getPostingList().findPostingByDocID(23567).get().getPositions().get(2),
                dogResult.getPostingList().findPostingByDocID(23567).get().getPositions().get(2));
    }

    private Example<PositionalIndex> getIndexExample() {
        return new Example<>(
                createIndex(),
                "cat:1[1,2,3]2[1,2,3]3[1]\ndog:1341[111,222,3333]23567[1,24,35678]3789067[15346373]"
        );
    }

    private PositionalIndex createIndex() {
        PositionalIndex index = new PositionalIndex();
        index.addTermOccurrences("cat", 1, 1, 2, 3);
        index.addTermOccurrences("cat", 2, 1, 2, 3);
        index.addTermOccurrence("cat", 3, 1);
        index.addTermOccurrences("dog", 1341, 111, 222, 3333);
        index.addTermOccurrences("dog", 23567, 1, 24, 35678);
        index.addTermOccurrence("dog", 3789067, 15346373);
        return index;
    }

    private Example<PositionalTerm> getTermExample() {
        return new Example<>(
                createTerm(),
                "cat:1[1,2,3]2[1,2,3]3[1]"
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

    private PositionalTerm createTerm() {
        PositionalPostingList postingList = new PositionalPostingList();
        postingList.addOccurrencesInDoc(1, 1, 2, 3);
        postingList.addOccurrencesInDoc(2, 1, 2, 3);
        postingList.addOccurrenceInDoc(3, 1);
        return new PositionalTerm("cat", postingList);
    }

    private PositionalPostingList createPostingList() {
        PositionalPostingList postingList = new PositionalPostingList();
        postingList.addOccurrencesInDoc(1, 1, 2, 3);
        postingList.addOccurrenceInDoc(2, 1);
        postingList.addOccurrenceInDoc(2, 2);
        postingList.addOccurrenceInDoc(2, 3);
        postingList.addOccurrenceInDoc(3, 1);
        return postingList;
    }

    private PositionalPosting createPosting() {
        return new PositionalPosting(1, 1, 2, 3);
    }

}
