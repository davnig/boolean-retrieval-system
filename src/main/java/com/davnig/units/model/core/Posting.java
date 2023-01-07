package com.davnig.units.model.core;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class Posting implements Comparable<Posting> {

    protected int docID;

    public Posting(int docID) {
        this.docID = docID;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Posting posting)) return false;
        return docID == posting.docID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(docID);
    }

    @Override
    public int compareTo(Posting other) {
        return Integer.compare(this.docID, other.docID);
    }

}
