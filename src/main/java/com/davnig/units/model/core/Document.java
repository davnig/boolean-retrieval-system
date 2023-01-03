package com.davnig.units.model.core;

public record Document<T>(
        int docID,
        T content
) {
}
