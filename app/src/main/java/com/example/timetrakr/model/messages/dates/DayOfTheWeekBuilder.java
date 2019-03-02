package com.example.timetrakr.model.messages.dates;

import com.example.timetrakr.model.messages.PartBuilder;

import java.time.LocalDateTime;

/**
 * Determines day of the week of the specified date.
 */
public class DayOfTheWeekBuilder implements PartBuilder<LocalDateTime> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildFrom(LocalDateTime time) {
        return time.getDayOfWeek().name().toLowerCase();
    }

}
