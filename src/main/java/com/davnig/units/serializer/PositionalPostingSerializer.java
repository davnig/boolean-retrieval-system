package com.davnig.units.serializer;

import com.davnig.units.exception.DeserializationException;
import com.davnig.units.model.PositionalPostings;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PositionalPostingSerializer implements Serializer<PositionalPostings> {
    @Override
    public String serialize(PositionalPostings input) {
        return input.toString();
    }

    @Override
    public PositionalPostings deserialize(String input) {
        Integer docID = extractDocID(input);
        List<Integer> positions = extractPositions(input);
        return new PositionalPostings(docID, positions);
    }

    private List<Integer> extractPositions(String input) {
        input = removeDocIDAndBrackets(input);
        return Arrays.stream(input.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private String removeDocIDAndBrackets(String input) {
        Pattern positionsPattern = Pattern.compile("(\\[(\\d*,?)*)");
        Matcher matcher = positionsPattern.matcher(input);
        if (matcher.find()) {
            return input.substring(matcher.start(), matcher.end())
                    .replace("[", "");
        }
        throw new DeserializationException("could not extract positions from posting");
    }

    private Integer extractDocID(String input) {
        Pattern docIDPattern = Pattern.compile("^\\d*");
        Matcher matcher = docIDPattern.matcher(input);
        if (matcher.find()) {
            return Integer.parseInt(input.substring(matcher.start(), matcher.end()));
        }
        throw new DeserializationException("could not extract docID from posting");
    }

}
