package com.example.timetrakr.model.messages.dates;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeOfTheDayBuilderTest {

    @Test
    public void test() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        LocalDateTime dateTime = LocalDateTime.of(2019, 3, 2, 7, 32);
        Assert.assertEquals("7:32", new TimeOfTheDayBuilder(formatter).buildFrom(dateTime));
        dateTime = dateTime.plusHours(10);
        Assert.assertEquals("17:32", new TimeOfTheDayBuilder(formatter).buildFrom(dateTime));
    }

}
