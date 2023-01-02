package com.davnig.units;

import com.davnig.units.model.Corpus;

public abstract class CorpusReader<T> {

    public abstract Corpus<T> loadInMemory();

}
