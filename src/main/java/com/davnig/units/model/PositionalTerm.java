package com.davnig.units.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class PositionalTerm implements Comparable<PositionalTerm>, Externalizable {

    private String word;
    private PositionalPostingList postingList;

    public PositionalTerm(String word) {
        this.word = word;
        this.postingList = new PositionalPostingList();
    }

    public PositionalTerm(String word, int docID, int position) {
        this(word);
        this.postingList.addPosting(docID, position);
    }

    public PositionalTerm(String word, int docID, int... position) {
        this(word);
        this.postingList.addPosting(docID, position);
    }

    public PositionalTerm(String word, PositionalPostingList postingList) {
        this.word = word;
        this.postingList = postingList;
    }

    /**
     * Adds a new {@link PositionalPosting} with the given parameters avoiding duplicates.
     *
     * @param docID    the document ID
     * @param position the term position inside the document
     */
    public void addPosting(int docID, int position) {
        postingList.addPosting(docID, position);
    }

    /**
     * Adds a new {@link PositionalPosting} with the given parameters avoiding duplicates.
     *
     * @param docID     the document ID
     * @param positions the array of term positions
     */
    public void addPosting(int docID, int... positions) {
        postingList.addPosting(docID, positions);
    }

    @Override
    public int compareTo(PositionalTerm term) {
        return word.compareTo(term.word);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionalTerm that)) return false;
        return Objects.equals(word, that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", word, postingList.toString());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(word);
        out.writeUTF(":");
        out.writeObject(postingList);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        word = (String) in.readObject();
        postingList = (PositionalPostingList) in.readObject();
    }

}
