package com.davnig.units.serializer;

import com.davnig.units.model.PositionalPostingList;
import com.davnig.units.model.PositionalTerm;

public class PositionalTermSerializer implements Serializer<PositionalTerm> {

    private Serializer<PositionalPostingList> postingListSerializer;

    @Override
    public String serialize(PositionalTerm input) {
        return input.toString();
    }

    @Override
    public PositionalTerm deserialize(String input) {
        String[] tokens = input.split(":");
        String word = tokens[0];
        String postingListAsString = tokens[1];
        postingListSerializer = new PositionalPostingListSerializer();
        PositionalPostingList postingList = postingListSerializer.deserialize(postingListAsString);
        return new PositionalTerm(word, postingList);
    }

}
