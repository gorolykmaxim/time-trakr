package com.example.timetrakr.model.messages.dates;

import com.example.timetrakr.model.messages.Condition;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Check if time is earlier than the specified one.
 */
public class IsTimeEarlierThan implements Condition<LocalDateTime> {


    private LocalTime localTime;

    /**
     * Construct a condition.
     *
     * @param localTime time to compare to
     */
    public IsTimeEarlierThan(LocalTime localTime) {
        this.localTime = localTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean appliesTo(LocalDateTime time) {
        return time.toLocalTime().isBefore(localTime);
    }

}
