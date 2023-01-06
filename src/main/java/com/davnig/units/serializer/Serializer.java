package com.davnig.units.serializer;

public interface Serializer<T> {

    String serialize(T input);

    T deserialize(String input);

}
