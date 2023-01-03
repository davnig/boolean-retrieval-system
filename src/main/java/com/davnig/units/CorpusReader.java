package com.davnig.units;

import com.davnig.units.model.Corpus;

public interface CorpusReader<T> {

    Corpus<T> loadCorpus();

}
