package com.davnig.units.model;

public record Document<T>(
        int docID,
        T content
) {
}
