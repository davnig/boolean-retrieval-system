package com.davnig.units.serializer;

import com.davnig.units.model.PositionalPosting;
import com.davnig.units.model.PositionalPostingList;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PositionalPostingListSerializer implements Serializer<PositionalPostingList> {

    private Serializer<PositionalPosting> postingSerializer;

    @Override
    public String serialize(PositionalPostingList input) {
        return input.toString();
    }

    @Override
    public PositionalPostingList deserialize(String input) {
        PositionalPostingList result = new PositionalPostingList();
        postingSerializer = new PositionalPostingSerializer();
        ArrayList<String> postingsAsString = extractPostingsAsString(input);
        postingsAsString.stream()
                .map(postingAsString -> postingSerializer.deserialize(postingAsString))
                .forEach(result::addPosting);
        return result;
    }

    private ArrayList<String> extractPostingsAsString(String postingListAsString) {
        ArrayList<String> postingsAsString = new ArrayList<>();
        Pattern postingPattern = Pattern.compile("(\\d*(\\[(\\d*,)*)\\d*\\Q]\\E)");
        Matcher matcher = postingPattern.matcher(postingListAsString);
        while (matcher.find()) {
            String encodedPosting = postingListAsString.substring(matcher.start(), matcher.end());
            postingsAsString.add(encodedPosting);
        }
        return postingsAsString;
    }

}
