package com.davnig.units.util;

import java.util.HashSet;
import java.util.Set;

public class SetUtils {

    /**
     * Intersects two sets in a non-destructive way.
     *
     * @param setA {@link Set<T>}
     * @param setB {@link Set<T>}
     * @param <T>
     * @return {@link Set<T>}
     */
    public static <T> Set<T> intersect(Set<T> setA, Set<T> setB) {
        Set<T> result = new HashSet<>(setA);
        result.retainAll(setB);
        return result;
    }

}
