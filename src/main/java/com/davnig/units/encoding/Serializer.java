package com.davnig.units.encoding;

public interface Serializer<T> {

    String serialize(T input);

    T deserialize(String input);

}
