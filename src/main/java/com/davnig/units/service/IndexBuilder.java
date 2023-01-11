package com.davnig.units.service;

import com.davnig.units.model.core.InvertedIndex;

public interface IndexBuilder<I extends InvertedIndex> {

    I build();

}
