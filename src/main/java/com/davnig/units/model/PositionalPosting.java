package com.davnig.units.model;

import com.davnig.units.model.core.Posting;

import java.util.ArrayList;

public class PositionalPosting extends Posting {

    private ArrayList<Integer> positions;

    public PositionalPosting(int docID) {
        this.docID = docID;
        this.positions = new ArrayList<>();
    }

    public void addPosition(int position) {
        positions.add(position);
    }

}
