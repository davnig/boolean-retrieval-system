package com.davnig.units.util;

/**
 * Utility class for {@link String} type.
 */
public class StringUtils {

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

}
