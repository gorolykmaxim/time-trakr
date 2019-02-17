package com.example.timetrakr.common;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

public class DurationFormatterTest {

    private DurationFormatter formatter;

    @Before
    public void setUp() throws Exception {
        formatter = new DurationFormatter();
    }

    @Test
    public void formatSeconds() {
        Assert.assertEquals("0m", formatter.format(Duration.ofSeconds(5)));
    }

    @Test
    public void formatMinutes() {
        Assert.assertEquals("23m", formatter.format(Duration.ofMinutes(23)));
    }

    @Test
    public void formatIncompleteMinutes() {
        Duration almostTwentyFourMinutes = Duration.ofMinutes(23).plusSeconds(59);
        Assert.assertEquals("23m", formatter.format(almostTwentyFourMinutes));
    }

    @Test
    public void formatHours() {
        Assert.assertEquals("5h", formatter.format(Duration.ofHours(5)));
    }

    @Test
    public void formatHoursAndMinutes() {
        Duration duration = Duration.ofHours(3).plusMinutes(23);
        Assert.assertEquals("3h 23m", formatter.format(duration));
    }
}
