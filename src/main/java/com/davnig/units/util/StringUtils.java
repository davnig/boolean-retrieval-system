package com.davnig.units.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for {@link String} type.
 */
public class StringUtils {

    public static String[] normalizeAndTokenize(String input, String regex) {
        return tokenize(normalize(input), regex);
    }

    /**
     * Remove punctuation and switch to lower case.
     *
     * @param input {@link String}
     * @return {@link String}
     */
    public static String normalize(String input) {
        return input.replaceAll("[^\\w^\\s-]", "")
                .toLowerCase();
    }

    /**
     * Tokenize by spaces.
     *
     * @param input {@link String}
     * @return array of tokens
     */
    public static String[] tokenize(String input) {
        return input.split(" ");
    }

    /**
     * Tokenize by applying the given regex.
     *
     * @param input {@link String}
     * @param regex {@link String}
     * @return array of tokens
     */
    public static String[] tokenize(String input, String regex) {
        return input.split(regex);
    }

    /**
     * Extracts three-grams from the given input.
     *
     * @param input a {@link String}
     * @return a {@link List} of {@link String} representing three-grams
     */
    public static List<String> extractThreeGrams(String input) {
        Pattern pattern = Pattern.compile(".{3}");
        Matcher matcher = pattern.matcher(input);
        ArrayList<String> threeGrams = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            if (matcher.find(i)) {
                threeGrams.add(input.substring(matcher.start(), matcher.end()));
            }
        }
        return threeGrams;
    }

    public static String addLeadingAndTrailingDollarSymbol(String input) {
        return String.format("$%s$", input);
    }

}
