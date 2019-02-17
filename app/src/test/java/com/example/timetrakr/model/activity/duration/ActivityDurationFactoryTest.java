package com.example.timetrakr.model.activity.duration;

import com.example.timetrakr.model.activity.events.ActivityStartEvent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public class ActivityDurationFactoryTest {

    private ActivityDurationFactory factory;
    private String swimmingActivityName, climbingActivityName;

    @Before
    public void setUp() throws Exception {
        factory = new ActivityDurationFactory();
        swimmingActivityName = "Swimming";
        climbingActivityName = "Climbing";
    }

    @Test
    public void testCreationFromTwoSequentialActivities() {
        LocalDateTime startTime = LocalDateTime.now();
        ActivityStartEvent event1 = new ActivityStartEvent(swimmingActivityName, startTime);
        ActivityStartEvent event2 = new ActivityStartEvent(climbingActivityName, startTime.plusHours(1));
        ActivityDuration duration = factory.createFromTwoSequentialEvents(event1, event2);
        Assert.assertEquals(swimmingActivityName, duration.getActivityName());
        Assert.assertEquals(Duration.ofHours(1), duration.getDuration());
    }

}
