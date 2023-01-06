package com.davnig.units.exception;

public class DeserializationException extends RuntimeException {

    public DeserializationException(String message) {
        System.err.printf("There was an error during index deserialization: %s", message);
    }

}
