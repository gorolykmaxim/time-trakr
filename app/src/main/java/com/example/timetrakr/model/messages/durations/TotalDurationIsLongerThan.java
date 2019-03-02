package com.example.timetrakr.model.messages.durations;

import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.example.timetrakr.model.messages.Condition;

import java.time.Duration;
import java.util.List;

/**
 * Check if total duration of all specified activities is longer than the specified value.
 */
public class TotalDurationIsLongerThan implements Condition<List<ActivityDuration>> {

    private Duration duration;

    /**
     * Construct a condition.
     *
     * @param duration duration to compare total duration of specified activities
     */
    public TotalDurationIsLongerThan(Duration duration) {
        this.duration = duration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean appliesTo(List<ActivityDuration> activityDurations) {
        Duration totalDuration = Duration.ZERO;
        for (ActivityDuration activityDuration: activityDurations) {
            totalDuration = totalDuration.plus(activityDuration.getDuration());
        }
        return totalDuration.compareTo(duration) > 0;
    }

}
