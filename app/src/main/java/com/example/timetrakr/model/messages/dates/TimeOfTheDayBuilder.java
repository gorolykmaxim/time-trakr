package com.example.timetrakr.model.messages.dates;

import com.example.timetrakr.model.messages.PartBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Determines time of the day of the specified date.
 */
public class TimeOfTheDayBuilder implements PartBuilder<LocalDateTime> {

    private DateTimeFormatter formatter;

    /**
     * Construct a builder.
     *
     * @param formatter date-time formatter to format built time
     */
    public TimeOfTheDayBuilder(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildFrom(LocalDateTime time) {
        return time.format(formatter);
    }

}
