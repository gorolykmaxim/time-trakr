package com.example.timetrakr.model.messages.durations;

import com.example.timetrakr.model.activity.duration.ActivityDuration;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HasDurationLongerThanTest {

    @Test
    public void test() {
        List<ActivityDuration> activityDurations = new ArrayList<>();
        activityDurations.add(new ActivityDuration("Jiggling", Duration.ofHours(2)));
        activityDurations.add(new ActivityDuration("Doing something actually useful", Duration.ofMinutes(10)));
        Assert.assertTrue(new HasDurationLongerThan(Duration.ofHours(1)).appliesTo(activityDurations));
        Assert.assertFalse(new HasDurationLongerThan(Duration.ofHours(2)).appliesTo(activityDurations));
        Assert.assertFalse(new HasDurationLongerThan(Duration.ofHours(3)).appliesTo(activityDurations));
    }

}
