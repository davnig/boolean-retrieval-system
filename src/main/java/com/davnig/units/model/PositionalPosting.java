package com.davnig.units.model;

import com.davnig.units.model.core.Posting;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PositionalPosting extends Posting {

    private final ArrayList<Integer> positions;

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
        addAllPositions(positions);
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
    public void addAllPositions(List<Integer> positions) {
        positions.forEach(this::addPosition);
    }

    /**
     * Same as {@link PositionalPosting#addPosition(int)} but with  multiple values,
     * which are assumed to be already ordered.
     *
     * @param positions the array of ordered positions to be added
     */
    public void addAllPositions(int... positions) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        Arrays.stream(positions).boxed().forEach(arrayList::add);
        addAllPositions(arrayList);
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
