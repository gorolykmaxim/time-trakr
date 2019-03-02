package com.example.timetrakr.model.messages.durations;

import com.example.timetrakr.common.DurationFormatter;
import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.example.timetrakr.model.messages.PartBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ActivityDurationWithDurationShorterThanBuilderTest {

    private List<ActivityDuration> activityDurations;
    private DurationFormatter formatter;

    @Before
    public void setUp() throws Exception {
        activityDurations = new ArrayList<>();
        activityDurations.add(new ActivityDuration("Making music", Duration.ofHours(2)));
        activityDurations.add(new ActivityDuration("Taking a shit", Duration.ofMinutes(10)));
        formatter = new DurationFormatter();
    }

    @Test
    public void thereIsAnActivityWithAShorterDuration() {
        PartBuilder<List<ActivityDuration>> builder = new ActivityDurationWithDurationShorterThanBuilder(Duration.ofMinutes(15), formatter);
        Assert.assertEquals(formatter.format(Duration.ofMinutes(10)), builder.buildFrom(activityDurations));
    }

    @Test(expected = NoActivityShorterThanError.class)
    public void thereIsNoActivityWithAShorterDuration() {
        PartBuilder<List<ActivityDuration>> builder = new ActivityDurationWithDurationShorterThanBuilder(Duration.ofSeconds(55), formatter);
        builder.buildFrom(activityDurations);
    }

}
