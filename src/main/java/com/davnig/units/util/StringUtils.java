package com.davnig.units.util;

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

}
