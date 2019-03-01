package com.example.timetrakr.model.messages;

/**
 * This error means, that you have set up your {@link MessageRepository} incorrectly.
 * Message repository should always contain a message, condition of which can always be met.
 * Otherwise, when not a single message condition applies to the specified input data, this
 * exception is thrown.
 */
public class NoMessageFoundError extends RuntimeException {

    /**
     * Construct the error.
     *
     * @param inputData input data that didn't met a single condition
     */
    public NoMessageFoundError(Object inputData) {
        super(String.format("No message found for '%s'", inputData));
    }
}
