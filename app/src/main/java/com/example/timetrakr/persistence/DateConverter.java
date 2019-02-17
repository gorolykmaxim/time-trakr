package com.example.timetrakr.persistence;

import androidx.room.TypeConverter;
import androidx.annotation.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Converter of {@link Date} instances.
 *
 * <p>We can't store actual dates in SQLite and yet we still need to do basic date-time comparisons
 * inside the database like "<", ">" and "=". This is why we are using timestamps in long.</p>
 */
public class DateConverter {

    /**
     * Create a date based on the specified timestamp.
     *
     * @param timestamp timestamp of the date
     * @return date-time that corresponds to the specified timestamp
     */
    @TypeConverter
    public static LocalDateTime toDateTime(@Nullable Long timestamp) {
        return timestamp != null ? LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()) : null;
    }

    /**
     * Create a timestamp of the specified time-time.
     *
     * @param time time to createNew timestamp of
     * @return timestamp of the specified time
     */
    @TypeConverter
    public static Long toTimestamp(@Nullable LocalDateTime time) {
        return time != null ? time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() : null;
    }

}
