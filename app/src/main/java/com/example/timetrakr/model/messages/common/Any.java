package com.example.timetrakr.model.messages.common;

import com.example.timetrakr.model.messages.Condition;

/**
 * Applies to any input data.
 */
public class Any <T> implements Condition<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean appliesTo(T t) {
        return true;
    }

}
