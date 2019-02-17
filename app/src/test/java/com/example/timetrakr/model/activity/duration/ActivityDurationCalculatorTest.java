package com.example.timetrakr.model.activity.duration;

import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.model.activity.events.ActivityStartEventFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityDurationCalculatorTest {

    private ActivityDurationCalculator calculator;

    @Before
    public void setUp() throws Exception {
        ActivityDurationFactory durationFactory = new ActivityDurationFactory();
        ActivityStartEventFactory startEventFactory = new ActivityStartEventFactory();
        calculator = new ActivityDurationCalculator(durationFactory, startEventFactory);
    }

    @Test
    public void calculateForThreeDifferentEvents() {
        List<ActivityStartEvent> activityStartEvents = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now().minusHours(7);
        // First we hung out at the bar for 1 hour
        activityStartEvents.add(new ActivityStartEvent("Drinking beer", time));
        time = time.plusHours(1);
        // Second we skated for another 2 hours
        activityStartEvents.add(new ActivityStartEvent("Skateboarding", time));
        time = time.plusHours(2);
        // And last but not least, we slept for 4 hours
        activityStartEvents.add(new ActivityStartEvent("Sleeping", time));
        List<ActivityDuration> activityDurations = calculator.calculateDurationsFromEvents(activityStartEvents);
        // We expect returned collection of activity durations to preserve original activity order
        ActivityDuration activityDuration = activityDurations.get(0);
        Assert.assertEquals("Drinking beer", activityDuration.getActivityName());
        Assert.assertEquals(Duration.ofHours(1), activityDuration.getDuration());
        activityDuration = activityDurations.get(1);
        Assert.assertEquals("Skateboarding", activityDuration.getActivityName());
        Assert.assertEquals(Duration.ofHours(2), activityDuration.getDuration());
        activityDuration = activityDurations.get(2);
        Assert.assertEquals("Sleeping", activityDuration.getActivityName());
        Assert.assertTrue(activityDuration.getDuration().compareTo(Duration.ofHours(4)) >= 0);
    }

    @Test
    public void calculateForActivityInterruptedByAnotherActivity() {
        List<ActivityStartEvent> activityStartEvents = new ArrayList<>();
        LocalDateTime time = LocalDateTime.now().minusHours(4);
        // First we drank some nice beer for 1 hour
        activityStartEvents.add(new ActivityStartEvent("Drinking beer", time));
        time = time.plusHours(1);
        // Then i've got interrupted by my boss, who requested my presence at work
        activityStartEvents.add(new ActivityStartEvent("Work", time));
        time = time.plusHours(1);
        // After 1 hour of tortures, i've managed to escape back to my buddies at the bar,
        // where we've spent another 2 nice hours
        activityStartEvents.add(new ActivityStartEvent("Drinking beer", time));
        List<ActivityDuration> activityDurations = calculator.calculateDurationsFromEvents(activityStartEvents);
        // We expect returned collection of activity durations to preserve original activity order
        ActivityDuration activityDuration = activityDurations.get(0);
        Assert.assertEquals("Drinking beer", activityDuration.getActivityName());
        Assert.assertTrue(activityDuration.getDuration().compareTo(Duration.ofHours(3)) >= 0);
        activityDuration = activityDurations.get(1);
        Assert.assertEquals("Work", activityDuration.getActivityName());
        Assert.assertEquals(Duration.ofHours(1), activityDuration.getDuration());
    }

}
