package com.example.timetrakr.model.messages.dates;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class IsTimeLaterThanTest {

    @Test
    public void test() {
        LocalDateTime dateTime = LocalDateTime.of(2019, 3, 2, 16, 40);
        // Should apply if time is later.
        Assert.assertTrue(new IsTimeLaterThan(LocalTime.of(16, 39)).appliesTo(dateTime));
        // Should not apply if time is now.
        Assert.assertFalse(new IsTimeLaterThan(LocalTime.of(16, 40)).appliesTo(dateTime));
        // Should not apply if time is earlier.
        Assert.assertFalse(new IsTimeLaterThan(LocalTime.of(16, 41)).appliesTo(dateTime));
    }

}
