package com.davnig.units.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PositionalPostingList implements Externalizable {

    private final List<PositionalPosting> postings;

    public PositionalPostingList() {
        postings = new ArrayList<>();
    }

    public Optional<PositionalPosting> findPostingByDocID(int docID) {
        return postings.stream()
                .filter(posting -> posting.getDocID() == docID)
                .findFirst();
    }

    public void addPosting(PositionalPosting posting) {
        addPosting(posting.getDocID(), posting.getPositions());
    }

    /**
     * Searches for a {@link PositionalPosting} in this list with the given {@code docID}.
     * If not present, creates a new one with empty positions.
     *
     * @param docID the document ID
     */
    public void addPosting(int docID) {
        Optional<PositionalPosting> queriedPosting = findPostingByDocID(docID);
        if (queriedPosting.isEmpty()) {
            postings.add(new PositionalPosting(docID));
        }
    }

    /**
     * Searches for a {@link PositionalPosting} in this list with the given {@code docID}.
     * If not present, creates a new one with the given parameters. If present, add
     * the given {@code position} avoiding duplicates.
     *
     * @param docID    the document ID
     * @param position the position to be added
     */
    public void addPosting(int docID, int position) {
        Optional<PositionalPosting> queriedPosting = findPostingByDocID(docID);
        if (queriedPosting.isEmpty()) {
            postings.add(new PositionalPosting(docID, position));
            return;
        }
        queriedPosting.get().addPosition(position);
    }

    /**
     * Same as {@link PositionalPostingList#addPosting(int, int)} but with multiple positions,
     * which are assumed to be already ordered.
     *
     * @param docID     the document ID
     * @param positions the array of positions to be added
     */
    public void addPosting(int docID, int... positions) {
        Optional<PositionalPosting> queriedPosting = findPostingByDocID(docID);
        if (queriedPosting.isEmpty()) {
            postings.add(new PositionalPosting(docID, positions));
            return;
        }
        queriedPosting.get().addAllPositions(positions);
    }

    /**
     * Same as {@link PositionalPostingList#addPosting(int, int)} but with multiple positions,
     * which are assumed to be already ordered.
     *
     * @param docID     the document ID
     * @param positions the list of positions to be added
     */
    public void addPosting(int docID, List<Integer> positions) {
        Optional<PositionalPosting> queriedPosting = findPostingByDocID(docID);
        if (queriedPosting.isEmpty()) {
            postings.add(new PositionalPosting(docID, positions));
            return;
        }
        queriedPosting.get().addAllPositions(positions);
    }

    public boolean isEmpty() {
        return postings.isEmpty();
    }

    public int size() {
        return postings.size();
    }

    @Override
    public String toString() {
        return postings.stream()
                .map(PositionalPosting::toString)
                .collect(Collectors.joining(""));
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        for (PositionalPosting posting : postings) {
            out.writeObject(posting);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Object object = in.readObject();
        List<Object> objectList = Collections.singletonList(object);
        objectList.forEach(obj -> postings.add((PositionalPosting) obj));
    }

}
