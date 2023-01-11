package com.davnig.units.service;

import com.davnig.units.model.core.Corpus;

public interface CorpusReader<T> {

    Corpus<T> loadCorpus();

}
