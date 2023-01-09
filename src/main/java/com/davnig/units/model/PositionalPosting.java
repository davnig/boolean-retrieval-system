package com.davnig.units.model;

import com.davnig.units.model.core.Posting;
import com.davnig.units.util.ListUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PositionalPosting extends Posting {

    private final List<Integer> positions;

    public PositionalPosting() {
        positions = new ArrayList<>();
    }

    public PositionalPosting(int docID) {
        super(docID);
        this.positions = new ArrayList<>();
    }

    public PositionalPosting(int docID, int position) {
        this(docID);
        this.positions.add(position);
    }

    public PositionalPosting(int docID, int... positions) {
        this(docID);
        addPositions(positions);
    }

    public PositionalPosting(int docID, List<Integer> positions) {
        this(docID);
        addPositions(positions);
    }

    /**
     * Adds the given position in this {@link PositionalPosting} avoiding duplicates.
     *
     * @param position the item to be added
     */
    public void addPosition(int position) {
        if (positions.stream().noneMatch(pos -> pos == position)) {
            positions.add(position);
        }
    }

    /**
     * Same as {@link PositionalPosting#addPosition(int)} but with  multiple values,
     * which are assumed to be already ordered.
     *
     * @param positions the list of ordered positions to be added
     */
    public void addPositions(List<Integer> positions) {
        positions.forEach(this::addPosition);
    }

    /**
     * Same as {@link PositionalPosting#addPosition(int)} but with  multiple values,
     * which are assumed to be already ordered.
     *
     * @param positions the array of ordered positions to be added
     */
    public void addPositions(int... positions) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        Arrays.stream(positions).boxed().forEach(arrayList::add);
        addPositions(arrayList);
    }

    public List<Integer> findAdjacentPositions(PositionalPosting other) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int thisPointer = 0, otherPointer = 0;
             ListUtils.hasNext(thisPointer, this.positions) && ListUtils.hasNext(otherPointer, other.positions); ) {
            Integer thisPosition = this.positions.get(thisPointer);
            Integer otherPosition = other.positions.get(otherPointer);
            if (thisPosition + 1 == otherPosition) {
                result.add(thisPosition);
                thisPointer++;
                otherPointer++;
            } else if (thisPosition < otherPosition) {
                thisPointer++;
            } else {
                otherPointer++;
            }
        }
        return result;
    }

    /**
     * Returns the number of positions stored in this {@link PositionalPosting}.
     *
     * @return number of positions
     */
    public int size() {
        return positions.size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(docID);
        stringBuilder.append("[");
        String positionsEncoding = positions.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        stringBuilder.append(positionsEncoding);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

}
