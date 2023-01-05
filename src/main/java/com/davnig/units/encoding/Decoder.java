package com.davnig.units.encoding;

public interface Decoder<T, R> {
    R decode(T input);
}
