package com.example.timetrakr.model.messages.common;

import com.example.timetrakr.model.messages.Condition;

import java.util.List;

/**
 * Check if size of the list is equal to the specified value.
 */
public class CountIs <T> implements Condition<List<T>> {

    private int count;

    /**
     * Construct a condition.
     *
     * @param count expected size of the list
     */
    public CountIs(int count) {
        this.count = count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean appliesTo(List list) {
        return list.size() == count;
    }

}
