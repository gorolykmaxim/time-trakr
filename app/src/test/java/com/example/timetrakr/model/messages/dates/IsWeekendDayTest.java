package com.example.timetrakr.model.messages.dates;

import com.example.timetrakr.model.messages.Condition;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class IsWeekendDayTest {

    @Test
    public void test() {
        Condition<LocalDateTime> condition = new IsWeekendDay();
        // Saturday check (yeah, it was written on the weekend).
        LocalDateTime dateTime = LocalDateTime.of(2019, 3, 2, 16, 27);
        Assert.assertTrue(condition.appliesTo(dateTime));
        // Sunday check.
        dateTime = dateTime.plusDays(1);
        Assert.assertTrue(condition.appliesTo(dateTime));
        // All other week days check
        for (int i = 0; i < 5; i++) {
            dateTime = dateTime.plusDays(1);
            Assert.assertFalse(condition.appliesTo(dateTime));
        }
    }

}
