package com.example.timetrakr.common;

import java.time.Duration;

/**
 * Formatter of {@link Duration}.
 *
 * <p>Formats durations in a following format: <pre>{hours}h {minutes}m</pre>.</p>
 *
 * <p>In case value of an hours or minutes is equal to 0, it is not displayed.</p>
 *
 */
public class DurationFormatter {

    /**
     * Format specified duration as a string.
     *
     * @param duration duration to format
     * @return formatted string representation of the duration
     */
    public String format(Duration duration) {
        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        minutes = minutes % 60;
        if (hours > 0 && minutes > 0) {
            return String.format("%sh %sm", hours, minutes);
        } else if (hours > 0) {
            return String.format("%sh", hours);
        } else {
            return String.format("%sm", minutes);
        }
    }

}
