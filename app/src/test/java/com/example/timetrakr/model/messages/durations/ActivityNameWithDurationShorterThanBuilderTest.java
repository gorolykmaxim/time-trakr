package com.example.timetrakr.model.messages.durations;

import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.example.timetrakr.model.messages.PartBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ActivityNameWithDurationShorterThanBuilderTest {

    private List<ActivityDuration> activityDurations;

    @Before
    public void setUp() throws Exception {
        activityDurations = new ArrayList<>();
        activityDurations.add(new ActivityDuration("Making music", Duration.ofHours(2)));
        activityDurations.add(new ActivityDuration("Taking a shit", Duration.ofMinutes(10)));
    }

    @Test
    public void thereIsAnActivityWithAShorterDuration() {
        PartBuilder<List<ActivityDuration>> builder = new ActivityNameWithDurationShorterThanBuilder(Duration.ofMinutes(15));
        Assert.assertEquals("Taking a shit", builder.buildFrom(activityDurations));
    }

    @Test(expected = NoActivityShorterThanError.class)
    public void thereIsNoActivityWithAShorterDuration() {
        PartBuilder<List<ActivityDuration>> builder = new ActivityNameWithDurationShorterThanBuilder(Duration.ofSeconds(55));
        builder.buildFrom(activityDurations);
    }

}
