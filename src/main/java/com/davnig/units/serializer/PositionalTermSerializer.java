package com.davnig.units.serializer;

import com.davnig.units.model.PositionalPostingsList;
import com.davnig.units.model.PositionalTerm;

public class PositionalTermSerializer implements Serializer<PositionalTerm> {

    private Serializer<PositionalPostingsList> postingsListSerializer;

    @Override
    public String serialize(PositionalTerm input) {
        return input.toString();
    }

    @Override
    public PositionalTerm deserialize(String input) {
        String[] tokens = input.split(":");
        String word = tokens[0];
        String postingsListAsString = tokens[1];
        postingsListSerializer = new PositionalPostingsListSerializer();
        PositionalPostingsList postingsList = postingsListSerializer.deserialize(postingsListAsString);
        return new PositionalTerm(word, postingsList);
    }

}
