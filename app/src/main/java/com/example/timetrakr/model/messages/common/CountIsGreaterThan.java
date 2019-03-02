package com.example.timetrakr.model.messages.common;

import com.example.timetrakr.model.messages.Condition;

import java.util.List;

/**
 * Check if size of the specified list is greater than the specified value.
 */
public class CountIsGreaterThan <T> implements Condition<List<T>> {

    private int count;

    /**
     * Construct a condition.
     *
     * @param count value to compare a list size to
     */
    public CountIsGreaterThan(int count) {
        this.count = count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean appliesTo(List<T> list) {
        return list.size() > count;
    }

}
