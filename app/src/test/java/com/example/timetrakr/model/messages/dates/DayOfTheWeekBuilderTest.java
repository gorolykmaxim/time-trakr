package com.example.timetrakr.model.messages.dates;

import com.example.timetrakr.model.messages.PartBuilder;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class DayOfTheWeekBuilderTest {

    @Test
    public void test() {
        PartBuilder<LocalDateTime> builder = new DayOfTheWeekBuilder();
        LocalDateTime dateTime = LocalDateTime.of(2019, 3, 4, 16, 22);
        Assert.assertEquals("monday", builder.buildFrom(dateTime));
        dateTime = dateTime.plusDays(1);
        Assert.assertEquals("tuesday", builder.buildFrom(dateTime));
        dateTime = dateTime.plusDays(1);
        Assert.assertEquals("wednesday", builder.buildFrom(dateTime));
        dateTime = dateTime.plusDays(1);
        Assert.assertEquals("thursday", builder.buildFrom(dateTime));
        dateTime = dateTime.plusDays(1);
        Assert.assertEquals("friday", builder.buildFrom(dateTime));
        dateTime = dateTime.plusDays(1);
        Assert.assertEquals("saturday", builder.buildFrom(dateTime));
        dateTime = dateTime.plusDays(1);
        Assert.assertEquals("sunday", builder.buildFrom(dateTime));
        dateTime = dateTime.plusDays(1);
        Assert.assertEquals("monday", builder.buildFrom(dateTime));
    }

}
