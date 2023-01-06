package com.davnig.units;

import com.davnig.units.model.PositionalIndex;

public class IRSystem {

    private static IRSystem instance;
    private final PositionalIndex index;

    private IRSystem(PositionalIndex index) {
        this.index = index;
    }

    private static IRSystem getInstance(PositionalIndex index) {
        if (instance == null) {
            instance = new IRSystem(index);
        }
        return instance;
    }

    public static void answer(PositionalIndex index, String query) {
        instance = IRSystem.getInstance(index);
    }

}
