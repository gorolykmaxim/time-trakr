package com.example.timetrakr.model.messages.durations;

import com.example.timetrakr.model.activity.duration.ActivityDuration;

import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TotalDurationIsLongerThanTest {

    @Test
    public void test() {
        List<ActivityDuration> activityDurations = new ArrayList<>();
        activityDurations.add(new ActivityDuration("Making this app", Duration.ofHours(55)));
        activityDurations.add(new ActivityDuration("Playing games on weekends", Duration.ofHours(4)));
        // Should apply if total duration is longer than the specified value.
        Assert.assertTrue(new TotalDurationIsLongerThan(Duration.ofHours(4)).appliesTo(activityDurations));
        // Should not apply if total duration is equal to the specified value.
        Assert.assertFalse(new TotalDurationIsLongerThan(Duration.ofHours(59)).appliesTo(activityDurations));
        // Should not apply if total duration is shorter than the specified value.
        Assert.assertFalse(new TotalDurationIsLongerThan(Duration.ofHours(69)).appliesTo(activityDurations));
    }

}
