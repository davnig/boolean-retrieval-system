package com.davnig.units.exception;

public class TermNotFoundInIndex extends RuntimeException {

    public TermNotFoundInIndex(String term) {
        System.err.printf("The term %s was not found in index%n", term);
    }

}
