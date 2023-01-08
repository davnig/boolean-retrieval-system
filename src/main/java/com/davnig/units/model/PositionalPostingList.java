package com.davnig.units.model;

import com.davnig.units.model.core.Posting;
import com.davnig.units.util.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PositionalPostingList {

    private final List<PositionalPosting> postings;

    public PositionalPostingList() {
        postings = new ArrayList<>();
    }

    public PositionalPostingList(List<PositionalPosting> postings) {
        this.postings = postings;
    }

    public List<Integer> getAllDocIDs() {
        return postings.stream()
                .map(Posting::getDocID)
                .collect(Collectors.toList());
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

    /**
     * Performs an intersection between this {@link PositionalPostingList} and the one provided as argument.
     *
     * @param other the second posting list
     * @return a {@link PositionalPostingList} representing the intersection
     */
    public PositionalPostingList intersect(PositionalPostingList other) {
        List<PositionalPosting> intersection = new ArrayList<>();
        for (int thisPointer = 0, otherPointer = 0;
             ListUtils.hasNext(thisPointer, this.postings) && ListUtils.hasNext(otherPointer, other.postings); ) {
            PositionalPosting thisPosting = this.postings.get(thisPointer);
            PositionalPosting otherPosting = other.postings.get(otherPointer);
            if (thisPosting.equals(otherPosting)) {
                intersection.add(new PositionalPosting(thisPosting.getDocID()));
                thisPointer++;
                otherPointer++;
            } else if (thisPosting.compareTo(otherPosting) < 0) {
                thisPointer++;
            } else {
                otherPointer++;
            }
        }
        return new PositionalPostingList(intersection);
    }

    /**
     * Performs an intersection between this {@link PositionalPostingList} and the one provided as argument, finding
     * the {@link PositionalPosting}s in common. Each of them is filled with the positions at which both terms are
     * adjacent in that document.
     *
     * @param other the second posting list
     * @return a {@link PositionalPostingList} representing the intersection
     */
    public PositionalPostingList intersectAndFillWithAdjacentPositions(PositionalPostingList other) {
        List<PositionalPosting> intersection = new ArrayList<>();
        for (int thisPointer = 0, otherPointer = 0;
             ListUtils.hasNext(thisPointer, this.postings) && ListUtils.hasNext(otherPointer, other.postings); ) {
            PositionalPosting thisPosting = this.postings.get(thisPointer);
            PositionalPosting otherPosting = other.postings.get(otherPointer);
            if (thisPosting.equals(otherPosting)) {
                PositionalPosting posting = new PositionalPosting(thisPosting.getDocID());
                posting.addAllPositions(thisPosting.findAdjacentPositions(otherPosting));
                intersection.add(posting);
                thisPointer++;
                otherPointer++;
            } else if (thisPosting.compareTo(otherPosting) < 0) {
                thisPointer++;
            } else {
                otherPointer++;
            }
        }
        return new PositionalPostingList(intersection);
    }

    /**
     * Performs a union between this {@link PositionalPostingList} and the one provided as argument.
     *
     * @param other the second posting list
     * @return a {@link PositionalPostingList} representing the union
     */
    public PositionalPostingList union(PositionalPostingList other) {
        List<PositionalPosting> union = new ArrayList<>();
        int thisPointer = 0, otherPointer = 0;
        while (ListUtils.hasNext(thisPointer, this.postings) && ListUtils.hasNext(otherPointer, other.postings)) {
            PositionalPosting thisPosting = this.postings.get(thisPointer);
            PositionalPosting otherPosting = other.postings.get(otherPointer);
            if (thisPosting.equals(otherPosting)) {
                union.add(new PositionalPosting(thisPosting.getDocID()));
                thisPointer++;
                otherPointer++;
            } else if (thisPosting.compareTo(otherPosting) < 0) {
                union.add(new PositionalPosting(thisPosting.getDocID()));
                thisPointer++;
            } else {
                union.add(new PositionalPosting(otherPosting.getDocID()));
                otherPointer++;
            }
        }
        unionAllRemaining(union, thisPointer, this.postings);
        unionAllRemaining(union, otherPointer, other.postings);
        return new PositionalPostingList(union);
    }

    private void unionAllRemaining(List<PositionalPosting> intermediateResult, int pointer, List<PositionalPosting> source) {
        for (; pointer < source.size(); pointer++) {
            intermediateResult.add(new PositionalPosting(source.get(pointer).getDocID()));
        }
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

}
