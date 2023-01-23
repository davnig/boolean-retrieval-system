package com.davnig.units.model.core;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class Postings implements Comparable<Postings> {

    protected int docID;

    public Postings(int docID) {
        this.docID = docID;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Postings postings)) return false;
        return docID == postings.docID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(docID);
    }

    @Override
    public int compareTo(Postings other) {
        return Integer.compare(this.docID, other.docID);
    }

}
