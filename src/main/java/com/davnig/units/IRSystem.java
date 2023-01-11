package com.davnig.units;

import com.davnig.units.model.core.Corpus;
import com.davnig.units.model.core.InvertedIndex;

public interface IRSystem<T, I extends InvertedIndex> {

    /**
     * Initializes the system with the given corpus and inverted index. This method must be performed prior to
     * any query.
     *
     * @param corpus a collection of documents of type {@link T}
     * @param index  an implementation of {@link InvertedIndex}
     */
    void start(Corpus<T> corpus, I index);

    /**
     * Parses the given query to either a boolean, a phrase or a wildcard query and performs the search of
     * documents of type {@link T}.
     *
     * @param query a boolean, phrase or wildcard query
     */
    void answer(String query);

}
