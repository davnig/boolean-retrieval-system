package com.davnig.units.model;

import com.davnig.units.model.core.PostingList;

public class PositionalPostingList extends PostingList<PositionalPosting> {

    @Override
    public void addPosting(int docID) {
        postings.add(new PositionalPosting(docID));
    }

}
