package com.davnig.units.encoding;

import com.davnig.units.model.PositionalPosting;
import com.davnig.units.model.PositionalPostingList;
import com.davnig.units.model.PositionalTerm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PositionalTermSerializer implements Serializer<PositionalTerm> {

    @Override
    public String serialize(PositionalTerm input) {
        return input.toString();
    }

    @Override
    public PositionalTerm deserialize(String input) {
        // get word
        String[] tokens = input.split(":");
        String word = tokens[0];
        String postingListAsString = tokens[1];
        PositionalPostingList postingList = decodePostingList(postingListAsString);
        return new PositionalTerm(word, postingList);
    }

    private PositionalPostingList decodePostingList(String postingListAsString) {
        // get encoded postings
        Pattern postingPattern = Pattern.compile("(\\d*(\\[(\\d*,)*)\\d*\\Q]\\E)");
        ArrayList<String> postingsAsString = new ArrayList<>();
        Matcher matcher = postingPattern.matcher(postingListAsString);
        while (matcher.find()) {
            String encodedPosting = postingListAsString.substring(matcher.start(), matcher.end());
            postingsAsString.add(encodedPosting);
        }
        // get docID and positions
        PositionalPostingList positionalPostingList = new PositionalPostingList();
        for (String postingAsString : postingsAsString) {
            decodePosting(postingAsString).ifPresent(positionalPostingList::addPosting);
        }
        return positionalPostingList;
    }

    private Optional<PositionalPosting> decodePosting(String postingAsString) {
        Pattern docIDPattern = Pattern.compile("^\\d*");
        Matcher matcher = docIDPattern.matcher(postingAsString);
        Integer docID = null;
        if (matcher.find()) {
            docID = Integer.parseInt(postingAsString.substring(matcher.start(), matcher.end()));
        }
        Pattern positionsPattern = Pattern.compile("(\\[(\\d*,?)*)");
        matcher = positionsPattern.matcher(postingAsString);
        String noDocID;
        List<Integer> positions = null;
        if (matcher.find()) {
            noDocID = postingAsString.substring(matcher.start(), matcher.end());
            noDocID = noDocID.replace("[", "");
            positions = Arrays.stream(noDocID.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
        if (docID != null && positions != null) {
            return Optional.of(new PositionalPosting(docID, positions));
        }
        return Optional.empty();
    }

}
