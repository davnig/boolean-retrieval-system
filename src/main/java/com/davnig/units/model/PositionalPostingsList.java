package com.davnig.units.model;

import com.davnig.units.model.core.Postings;
import com.davnig.units.util.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PositionalPostingsList {

    private final List<PositionalPostings> postings;

    public PositionalPostingsList() {
        postings = new ArrayList<>();
    }

    public PositionalPostingsList(List<PositionalPostings> postings) {
        this.postings = postings;
    }

    public List<Integer> getAllDocIDs() {
        return postings.stream()
                .map(Postings::getDocID)
                .collect(Collectors.toList());
    }

    public Optional<PositionalPostings> findPostingByDocID(int docID) {
        return postings.stream()
                .filter(posting -> posting.getDocID() == docID)
                .findFirst();
    }

    /**
     * Adds the given posting to this list. This method assumes the absence of any
     * other posting with the same doc ID.
     *
     * @param posting a {@link PositionalPostings}
     */
    public void addPosting(PositionalPostings posting) {
        postings.add(posting);
    }

    /**
     * Searches for a {@link PositionalPostings} in this list with the given {@code docID}.
     * If not present, creates a new one with the given parameters. If present, add
     * the given {@code position} avoiding duplicates.
     *
     * @param docID    the document ID
     * @param position the position to be added
     */
    public void addOccurrenceInDoc(int docID, int position) {
        Optional<PositionalPostings> queriedPosting = findPostingByDocID(docID);
        if (queriedPosting.isEmpty()) {
            postings.add(new PositionalPostings(docID, position));
            return;
        }
        queriedPosting.get().addPosition(position);
    }

    /**
     * Same as {@link PositionalPostingsList#addOccurrenceInDoc(int, int)} but with multiple positions,
     * which are assumed to be already ordered.
     *
     * @param docID     the document ID
     * @param positions the array of positions to be added
     */
    public void addOccurrencesInDoc(int docID, int... positions) {
        Optional<PositionalPostings> queriedPosting = findPostingByDocID(docID);
        if (queriedPosting.isEmpty()) {
            postings.add(new PositionalPostings(docID, positions));
            return;
        }
        queriedPosting.get().addPositions(positions);
    }

    /**
     * Same as {@link PositionalPostingsList#addOccurrenceInDoc(int, int)} but with multiple positions,
     * which are assumed to be already ordered.
     *
     * @param docID     the document ID
     * @param positions the list of positions to be added
     */
    public void addOccurrencesInDoc(int docID, List<Integer> positions) {
        Optional<PositionalPostings> queriedPosting = findPostingByDocID(docID);
        if (queriedPosting.isEmpty()) {
            postings.add(new PositionalPostings(docID, positions));
            return;
        }
        queriedPosting.get().addPositions(positions);
    }

    /**
     * Performs an intersection between this {@link PositionalPostingsList} and the one provided as argument.
     *
     * @param other the second posting list
     * @return a {@link PositionalPostingsList} representing the intersection
     */
    public PositionalPostingsList intersect(PositionalPostingsList other) {
        List<PositionalPostings> intersection = new ArrayList<>();
        for (int thisPointer = 0, otherPointer = 0;
             ListUtils.hasNext(thisPointer, this.postings) && ListUtils.hasNext(otherPointer, other.postings); ) {
            PositionalPostings thisPosting = this.postings.get(thisPointer);
            PositionalPostings otherPosting = other.postings.get(otherPointer);
            if (thisPosting.equals(otherPosting)) {
                intersection.add(new PositionalPostings(thisPosting.getDocID()));
                thisPointer++;
                otherPointer++;
            } else if (thisPosting.compareTo(otherPosting) < 0) {
                thisPointer++;
            } else {
                otherPointer++;
            }
        }
        return new PositionalPostingsList(intersection);
    }

    /**
     * Performs an intersection between this {@link PositionalPostingsList} and the one provided as argument, finding
     * the {@link PositionalPostings}s in common. Each of them is filled with the positions at which both terms are
     * adjacent in that document.
     *
     * @param other the second posting list
     * @return a {@link PositionalPostingsList} representing the intersection
     */
    public PositionalPostingsList intersectAndFillWithAdjacentPositions(PositionalPostingsList other) {
        List<PositionalPostings> intersection = new ArrayList<>();
        for (int thisPointer = 0, otherPointer = 0;
             ListUtils.hasNext(thisPointer, this.postings) && ListUtils.hasNext(otherPointer, other.postings); ) {
            PositionalPostings thisPosting = this.postings.get(thisPointer);
            PositionalPostings otherPosting = other.postings.get(otherPointer);
            if (thisPosting.equals(otherPosting)) {
                PositionalPostings posting = new PositionalPostings(thisPosting.getDocID());
                posting.addPositions(thisPosting.findAdjacentPositions(otherPosting));
                intersection.add(posting);
                thisPointer++;
                otherPointer++;
            } else if (thisPosting.compareTo(otherPosting) < 0) {
                thisPointer++;
            } else {
                otherPointer++;
            }
        }
        return new PositionalPostingsList(intersection);
    }

    /**
     * Performs a union between this {@link PositionalPostingsList} and the one provided as argument.
     *
     * @param other the second posting list
     * @return a {@link PositionalPostingsList} representing the union
     */
    public PositionalPostingsList union(PositionalPostingsList other) {
        List<PositionalPostings> union = new ArrayList<>();
        int thisPointer = 0, otherPointer = 0;
        while (ListUtils.hasNext(thisPointer, this.postings) && ListUtils.hasNext(otherPointer, other.postings)) {
            PositionalPostings thisPosting = this.postings.get(thisPointer);
            PositionalPostings otherPosting = other.postings.get(otherPointer);
            if (thisPosting.equals(otherPosting)) {
                union.add(new PositionalPostings(thisPosting.getDocID()));
                thisPointer++;
                otherPointer++;
            } else if (thisPosting.compareTo(otherPosting) < 0) {
                union.add(new PositionalPostings(thisPosting.getDocID()));
                thisPointer++;
            } else {
                union.add(new PositionalPostings(otherPosting.getDocID()));
                otherPointer++;
            }
        }
        unionAllRemaining(union, thisPointer, this.postings);
        unionAllRemaining(union, otherPointer, other.postings);
        return new PositionalPostingsList(union);
    }

    private void unionAllRemaining(List<PositionalPostings> intermediateResult, int pointer, List<PositionalPostings> source) {
        for (; pointer < source.size(); pointer++) {
            intermediateResult.add(new PositionalPostings(source.get(pointer).getDocID()));
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
                .map(PositionalPostings::toString)
                .collect(Collectors.joining(""));
    }

}
