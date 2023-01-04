package com.davnig.units.model;

import com.davnig.units.model.core.Posting;

import java.util.ArrayList;

public class PositionalPosting extends Posting {

    private ArrayList<Integer> positions;

    public PositionalPosting(int docID) {
        super(docID);
        this.positions = new ArrayList<>();
    }

    public void addPosition(int position) {
        positions.add(position);
    }

    public ArrayList<Integer> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Integer> positions) {
        this.positions = positions;
    }

}
