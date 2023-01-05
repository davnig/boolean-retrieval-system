package com.davnig.units.util;

import java.util.List;

public class ListUtils {

    public static <T> boolean isLastPositionInList(int position, List<T> list) {
        return position == list.size() - 1;
    }

}
