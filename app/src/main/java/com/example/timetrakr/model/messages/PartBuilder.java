package com.example.timetrakr.model.messages;

/**
 * Obtains message parts from the specified input data, to build dynamic messages.
 */
public interface PartBuilder <T> {

    /**
     * Analyze specified input data and build a corresponding message part.
     *
     * @param t input data to analyze
     * @return message part that corresponds to the specified input data
     */
    String buildFrom(T t);

}
