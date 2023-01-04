package com.davnig.units.model;

import com.davnig.units.model.core.Posting;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class PositionalPosting extends Posting {

    private ArrayList<Integer> positions;

    public PositionalPosting(int docID) {
        super(docID);
        this.positions = new ArrayList<>();
    }

    public PositionalPosting(int docID, int position) {
        this(docID);
        this.positions.add(position);
    }

    public void addPosition(int position) {
        positions.add(position);
    }

    public void addAllPositions(ArrayList<Integer> positions) {
        this.positions.addAll(positions);
    }

}
