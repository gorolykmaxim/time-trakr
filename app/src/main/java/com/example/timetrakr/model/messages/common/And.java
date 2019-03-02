package com.example.timetrakr.model.messages.common;

import com.example.timetrakr.model.messages.Condition;

/**
 * Check if both of the specified conditions apply to the specified input data.
 */
public class And <T> implements Condition<T> {

    private Condition<T> conditionA, conditionB;

    /**
     * Construct a condition.
     *
     * @param conditionA first condition, that should apply
     * @param conditionB second condition, that should apply
     */
    public And(Condition<T> conditionA, Condition<T> conditionB) {
        this.conditionA = conditionA;
        this.conditionB = conditionB;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean appliesTo(T t) {
        return conditionA.appliesTo(t) && conditionB.appliesTo(t);
    }

}
