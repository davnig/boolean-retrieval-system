package com.davnig.units.serializer;

import com.davnig.units.model.PositionalPostingList;
import com.davnig.units.model.PositionalTerm;

import static com.davnig.units.util.StringUtils.extractThreeGrams;

public class PositionalTermSerializer implements Serializer<PositionalTerm> {

    private Serializer<PositionalPostingList> postingListSerializer;

    @Override
    public String serialize(PositionalTerm input) {
        return String.format("%s:%s",
                input.toString(),
                String.join(",", extractThreeGrams(input.getWord())));
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
