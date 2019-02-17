package com.example.timetrakr.persistence;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateConverterTest {
    
    private long expectedTimestamp;
    private LocalDateTime expectedLocalDateTime;

    @Before
    public void setUp() throws Exception {
        expectedTimestamp = System.currentTimeMillis();
        expectedLocalDateTime = Instant.ofEpochMilli(expectedTimestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Test
    public void fromLocalDateTimeToLong() {
        long actualTime = DateConverter.toTimestamp(expectedLocalDateTime);
        Assert.assertEquals(expectedTimestamp, actualTime);
    }

    @Test
    public void fromNullToLong() {
        Assert.assertNull(DateConverter.toTimestamp(null));
    }

    @Test
    public void fromTimestampToLocalDateTime() {
        LocalDateTime actualLocalDateTime = DateConverter.toDateTime(expectedTimestamp);
        Assert.assertEquals(expectedLocalDateTime, actualLocalDateTime);
    }

    @Test
    public void fromNullToLocalDateTime() {
        Assert.assertNull(DateConverter.toDateTime(null));
    }

}
