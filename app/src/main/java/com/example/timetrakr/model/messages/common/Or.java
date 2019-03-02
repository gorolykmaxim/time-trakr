package com.example.timetrakr.model.messages.common;

import com.example.timetrakr.model.messages.Condition;

/**
 * Check if any of the specified conditions apply to the specified input data.
 */
public class Or <T> implements Condition<T> {

    private Condition<T>[] conditions;

    /**
     * Construct a condition.
     *
     * @param conditions conditions, one of which should apply
     */
    public Or(Condition<T>... conditions) {
        this.conditions = conditions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean appliesTo(T t) {
        for (Condition<T> condition: conditions) {
            if (condition.appliesTo(t)) {
                return true;
            }
        }
        return false;
    }

}
