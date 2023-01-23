package com.davnig.units.serializer;

import com.davnig.units.model.PositionalPostings;
import com.davnig.units.model.PositionalPostingsList;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PositionalPostingsListSerializer implements Serializer<PositionalPostingsList> {

    private Serializer<PositionalPostings> postingSerializer;

    @Override
    public String serialize(PositionalPostingsList input) {
        return input.toString();
    }

    @Override
    public PositionalPostingsList deserialize(String input) {
        PositionalPostingsList result = new PositionalPostingsList();
        postingSerializer = new PositionalPostingSerializer();
        ArrayList<String> postingsAsString = extractPostingsAsString(input);
        postingsAsString.stream()
                .map(postingAsString -> postingSerializer.deserialize(postingAsString))
                .forEach(result::addPosting);
        return result;
    }

    private ArrayList<String> extractPostingsAsString(String postingsListAsString) {
        ArrayList<String> postingsAsString = new ArrayList<>();
        Pattern postingPattern = Pattern.compile("(\\d*(\\[(\\d*,)*)\\d*\\Q]\\E)");
        Matcher matcher = postingPattern.matcher(postingsListAsString);
        while (matcher.find()) {
            String encodedPosting = postingsListAsString.substring(matcher.start(), matcher.end());
            postingsAsString.add(encodedPosting);
        }
        return postingsAsString;
    }

}
