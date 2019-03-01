package com.example.timetrakr.model.messages;

/**
 * A boolean condition, applied to incoming data to determine whether or not message, bind to
 * this condition, can be displayed right now.
 */
public interface Condition <T> {

    /**
     * Return true if specified input data satisfies this condition.
     *
     * @param t input data to check
     * @return true if input data satisfies this condition
     */
    boolean appliesTo(T t);

}
