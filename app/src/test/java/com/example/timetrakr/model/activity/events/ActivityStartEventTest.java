package com.example.timetrakr.model.activity.events;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class ActivityStartEventTest {

    @Test
    public void createActivityStartEvent() {
        String hiking = "Hiking";
        LocalDateTime dateTime = LocalDateTime.now();
        ActivityStartEvent event = new ActivityStartEvent(hiking, dateTime);
        Assert.assertEquals(hiking, event.getActivityName());
        Assert.assertEquals(dateTime, event.getStartDate());
    }

    @Test
    public void twoActivityStartEventsEqual() {
        String hiking = "Hiking";
        LocalDateTime dateTime = LocalDateTime.now();
        ActivityStartEvent activityStartEvent1 = new ActivityStartEvent(hiking, dateTime);
        ActivityStartEvent activityStartEvent2 = new ActivityStartEvent(hiking, dateTime);
        Assert.assertEquals(activityStartEvent1, activityStartEvent2);
        Assert.assertEquals(activityStartEvent1.hashCode(), activityStartEvent2.hashCode());
    }

    @Test
    public void twoActivityStartEventsAreNotEqual() {
        String bowling = "Bowling";
        ActivityStartEvent activityStartEvent1 = new ActivityStartEvent(bowling, LocalDateTime.now());
        ActivityStartEvent activityStartEvent2 = new ActivityStartEvent(bowling, LocalDateTime.now().minusMinutes(15));
        Assert.assertNotEquals(activityStartEvent1, activityStartEvent2);
        Assert.assertNotEquals(activityStartEvent1.hashCode(), activityStartEvent2.hashCode());
    }
}
