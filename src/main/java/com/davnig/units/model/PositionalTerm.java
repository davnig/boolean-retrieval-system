package com.davnig.units.model;

import com.davnig.units.model.core.Term;

import java.util.Optional;

public class PositionalTerm extends Term<PositionalPostingList>
        implements Comparable<PositionalTerm> {

    public PositionalTerm(String word) {
        super(word);
        this.postingList = new PositionalPostingList();
    }

    public PositionalTerm(String word, PositionalPostingList postingList) {
        super(word, postingList);
    }

    @Override
    public int compareTo(PositionalTerm term) {
        return word.compareTo(term.word);
    }

    public void addPosition(int docID, int position) {
        Optional<PositionalPosting> postingByDocID = postingList.findPostingByDocID(docID);
        if (postingByDocID.isPresent()) {
            postingByDocID.get().addPosition(position);
        } else {
            postingList.addPosting(docID);
            postingList.findPostingByDocID(docID).ifPresent(positionalPosting -> positionalPosting.addPosition(position));
        }
    }

}
