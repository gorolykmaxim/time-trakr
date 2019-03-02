package com.example.timetrakr.model.messages.durations;

import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.example.timetrakr.model.messages.PartBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ActivityNameWithDurationLongerThanBuilderTest {

    private List<ActivityDuration> activityDurations;

    @Before
    public void setUp() throws Exception {
        activityDurations = new ArrayList<>();
        activityDurations.add(new ActivityDuration("Making music", Duration.ofHours(2)));
        activityDurations.add(new ActivityDuration("Taking a shit", Duration.ofMinutes(10)));
    }

    @Test
    public void thereIsAnActivityWithALongerDuration() {
        PartBuilder<List<ActivityDuration>> builder = new ActivityNameWithDurationLongerThanBuilder(Duration.ofMinutes(15));
        Assert.assertEquals("Making music", builder.buildFrom(activityDurations));
    }

    @Test(expected = NoActivityLongerThanError.class)
    public void thereIsNoActivityWithALongerDuration() {
        PartBuilder<List<ActivityDuration>> builder = new ActivityNameWithDurationLongerThanBuilder(Duration.ofHours(2));
        builder.buildFrom(activityDurations);
    }

}
