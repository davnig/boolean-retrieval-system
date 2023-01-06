package com.davnig.units;

import com.davnig.units.model.MovieIndexBuilder;
import com.davnig.units.model.PositionalIndex;

public class Main {

    public static void main(String[] args) {
        PositionalIndex dictionary = MovieIndexBuilder.build("data/index.txt");
        IRSystem.answer(dictionary, "cat AND dog");
    }

}