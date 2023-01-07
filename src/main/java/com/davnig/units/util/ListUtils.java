package com.davnig.units.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static <T> boolean isLastPositionInList(int position, List<T> list) {
        return position == list.size() - 1;
    }

    public static <T> boolean hasNext(int position, List<T> list) {
        return position < list.size();
    }

    public static <T> List<T> getAllRemaining(int position, List<T> list) {
        ArrayList<T> allRemaining = new ArrayList<>();
        for (; position < list.size(); position++) {
            allRemaining.add(list.get(position));
        }
        return allRemaining;
    }

}
