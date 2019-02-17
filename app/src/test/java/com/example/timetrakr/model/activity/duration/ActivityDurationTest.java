package com.example.timetrakr.model.activity.duration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

public class ActivityDurationTest {

    private String activityName;

    @Before
    public void setUp() throws Exception {
        activityName = "Sports walking";
    }

    @Test
    public void createDuration() {
        ActivityDuration activityDuration = new ActivityDuration(activityName, Duration.ofHours(1));
        Assert.assertEquals(activityName, activityDuration.getActivityName());
        Assert.assertEquals(Duration.ofHours(1), activityDuration.getDuration());
    }

    @Test
    public void prolongDuration() {
        ActivityDuration activityDuration = new ActivityDuration(activityName, Duration.ofHours(1));
        activityDuration = activityDuration.prolongBy(Duration.ofHours(1));
        Assert.assertEquals(activityName, activityDuration.getActivityName());
        Assert.assertEquals(Duration.ofHours(2), activityDuration.getDuration());
    }

    @Test
    public void twoActivityDurationsAreEqual() {
        ActivityDuration activityDuration1 = new ActivityDuration(activityName, Duration.ofHours(1));
        ActivityDuration activityDuration2 = new ActivityDuration(activityName, Duration.ofMinutes(60));
        Assert.assertEquals(activityDuration1, activityDuration2);
        Assert.assertEquals(activityDuration1.hashCode(), activityDuration2.hashCode());
    }

    @Test
    public void twoActivityDurationsAreNotEqual() {
        ActivityDuration activityDuration1 = new ActivityDuration(activityName, Duration.ofHours(1));
        ActivityDuration activityDuration2 = new ActivityDuration("Similar activity", Duration.ofHours(1));
        Assert.assertNotEquals(activityDuration1, activityDuration2);
        activityDuration2 = new ActivityDuration(activityName, Duration.ofMinutes(59));
        Assert.assertNotEquals(activityDuration1, activityDuration2);
    }

}
