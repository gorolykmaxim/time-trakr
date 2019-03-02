package com.example.timetrakr.model.messages.durations;

import com.example.timetrakr.model.activity.duration.ActivityDuration;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HasDurationShorterThanTest {

    @Test
    public void test() {
        List<ActivityDuration> activityDurations = new ArrayList<>();
        activityDurations.add(new ActivityDuration("Watching TV shows", Duration.ofHours(3)));
        activityDurations.add(new ActivityDuration("Doing a home work", Duration.ofMinutes(5)));
        Assert.assertTrue(new HasDurationShorterThan(Duration.ofHours(1)).appliesTo(activityDurations));
        Assert.assertFalse(new HasDurationShorterThan(Duration.ofMinutes(5)).appliesTo(activityDurations));
        Assert.assertFalse(new HasDurationShorterThan(Duration.ofMinutes(4)).appliesTo(activityDurations));
    }

}
