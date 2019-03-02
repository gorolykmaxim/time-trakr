package com.example.timetrakr.model.messages.common;

import com.example.timetrakr.model.messages.Condition;

/**
 * Inverts the result of the specified condition, e.g. if specified condition does apply to the
 * specified input data, this condition does not and vice-versa.
 */
public class Not <T> implements Condition<T> {

    private Condition<T> condition;

    /**
     * Construct a condition.
     *
     * @param condition condition to invert result of
     */
    public Not(Condition<T> condition) {
        this.condition = condition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean appliesTo(T t) {
        return !condition.appliesTo(t);
    }

}
