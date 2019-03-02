package com.example.timetrakr.model.messages.dates;

import com.example.timetrakr.model.messages.Condition;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Check if time is later than the specified one.
 */
public class IsTimeLaterThan implements Condition<LocalDateTime> {

    private LocalTime localTime;

    /**
     * Construct a condition.
     *
     * @param localTime time to compare to
     */
    public IsTimeLaterThan(LocalTime localTime) {
        this.localTime = localTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean appliesTo(LocalDateTime time) {
        return time.toLocalTime().isAfter(localTime);
    }

}
