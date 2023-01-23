package com.davnig.units;

import com.davnig.units.model.PositionalPosting;
import com.davnig.units.model.PositionalPostingsList;
import com.davnig.units.model.PositionalTerm;
import com.davnig.units.serializer.PositionalPostingSerializer;
import com.davnig.units.serializer.PositionalPostingsListSerializer;
import com.davnig.units.serializer.PositionalTermSerializer;
import com.davnig.units.serializer.Serializer;
import com.davnig.units.service.PositionalIndex;
import com.davnig.units.service.impl.BSTPositionalIndex;
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
    void given_postingsList_when_serialize_should_correctlyEncode() {
        Serializer<PositionalPostingsList> serializer = new PositionalPostingsListSerializer();
        Example<PositionalPostingsList> example = getPostingsListExample();
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
    void given_encodedPostingsList_when_deserialize_should_correctlyDecode() {
        Serializer<PositionalTerm> serializer = new PositionalTermSerializer();
        Example<PositionalTerm> termExample = getTermExample();
        PositionalTerm result = serializer.deserialize(termExample.string);
        assertEquals(termExample.object, result);
        assertEquals(3, result.getPostingsList().size());
        assertTrue(result.getPostingsList().findPostingByDocID(1).isPresent());
        assertTrue(result.getPostingsList().findPostingByDocID(2).isPresent());
        assertTrue(result.getPostingsList().findPostingByDocID(3).isPresent());
        PositionalPosting firstPosting = result.getPostingsList().findPostingByDocID(1).get();
        PositionalPosting secondPosting = result.getPostingsList().findPostingByDocID(2).get();
        PositionalPosting thirdPosting = result.getPostingsList().findPostingByDocID(3).get();
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
        PositionalIndex result = new BSTPositionalIndex();
        try (
                BufferedReader reader = new BufferedReader(stringReader)
        ) {
            reader.lines()
                    .map(serializer::deserialize)
                    .forEach(result::addTerm);
        } catch (IOException e) {
            fail();
        }
        result.invertedIndexIterator().forEachRemaining(System.out::println);
        assertTrue(result.existsTermByWord("cat"));
        assertTrue(result.existsTermByWord("dog"));
        PositionalTerm expectedCat = indexExample.object.findTermByWord("cat").get();
        PositionalTerm expectedDog = indexExample.object.findTermByWord("dog").get();
        PositionalTerm catResult = result.findTermByWord("cat").get();
        PositionalTerm dogResult = result.findTermByWord("dog").get();
        assertEquals(expectedCat.getPostingsList().size(), catResult.getPostingsList().size());
        assertEquals(expectedDog.getPostingsList().size(), dogResult.getPostingsList().size());
        assertTrue(catResult.getPostingsList().findPostingByDocID(2).isPresent());
        assertTrue(dogResult.getPostingsList().findPostingByDocID(23567).isPresent());
        assertEquals(expectedCat.getPostingsList().findPostingByDocID(2).get().getPositions().get(2),
                catResult.getPostingsList().findPostingByDocID(2).get().getPositions().get(2));
        assertEquals(expectedDog.getPostingsList().findPostingByDocID(23567).get().getPositions().get(2),
                dogResult.getPostingsList().findPostingByDocID(23567).get().getPositions().get(2));
    }

    private Example<PositionalIndex> getIndexExample() {
        return new Example<>(
                createIndex(),
                "cat:1[1,2,3]2[1,2,3]3[1]\ndog:1341[111,222,3333]23567[1,24,35678]3789067[15346373]"
        );
    }

    private PositionalIndex createIndex() {
        PositionalIndex index = new BSTPositionalIndex();
        index.addTermAndOccurrences("cat", 1, 1, 2, 3);
        index.addTermAndOccurrences("cat", 2, 1, 2, 3);
        index.addTermAndOccurrence("cat", 3, 1);
        index.addTermAndOccurrences("dog", 1341, 111, 222, 3333);
        index.addTermAndOccurrences("dog", 23567, 1, 24, 35678);
        index.addTermAndOccurrence("dog", 3789067, 15346373);
        return index;
    }

    private Example<PositionalTerm> getTermExample() {
        return new Example<>(
                createTerm(),
                "cat:1[1,2,3]2[1,2,3]3[1]"
        );
    }

    private Example<PositionalPostingsList> getPostingsListExample() {
        return new Example<>(
                createPostingsList(),
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
        PositionalPostingsList postingsList = new PositionalPostingsList();
        postingsList.addOccurrencesInDoc(1, 1, 2, 3);
        postingsList.addOccurrencesInDoc(2, 1, 2, 3);
        postingsList.addOccurrenceInDoc(3, 1);
        return new PositionalTerm("cat", postingsList);
    }

    private PositionalPostingsList createPostingsList() {
        PositionalPostingsList postingsList = new PositionalPostingsList();
        postingsList.addOccurrencesInDoc(1, 1, 2, 3);
        postingsList.addOccurrenceInDoc(2, 1);
        postingsList.addOccurrenceInDoc(2, 2);
        postingsList.addOccurrenceInDoc(2, 3);
        postingsList.addOccurrenceInDoc(3, 1);
        return postingsList;
    }

    private PositionalPosting createPosting() {
        return new PositionalPosting(1, 1, 2, 3);
    }

}
