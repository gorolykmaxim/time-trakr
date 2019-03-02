package com.example.timetrakr.model.messages.dates;

import com.example.timetrakr.model.messages.Condition;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Check if specified date is a weekend day.
 */
public class IsWeekendDay implements Condition<LocalDateTime> {

    private Set<DayOfWeek> weekendDays;

    /**
     * Construct a condition.
     */
    public IsWeekendDay() {
        weekendDays = new HashSet<>();
        weekendDays.add(DayOfWeek.SATURDAY);
        weekendDays.add(DayOfWeek.SUNDAY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean appliesTo(LocalDateTime time) {
        return weekendDays.contains(time.getDayOfWeek());
    }

}
