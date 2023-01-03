package com.davnig.units;

import com.davnig.units.model.core.Corpus;

public interface CorpusReader<T> {

    Corpus<T> loadCorpus();

}
