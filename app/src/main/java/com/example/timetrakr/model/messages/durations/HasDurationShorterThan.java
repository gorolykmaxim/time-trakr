package com.example.timetrakr.model.messages.durations;

import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.example.timetrakr.model.messages.Condition;

import java.time.Duration;
import java.util.List;

/**
 * Check if specified list has an activity, duration of which is shorter than the specified value.
 */
public class HasDurationShorterThan implements Condition<List<ActivityDuration>> {

    private Duration duration;

    /**
     * Construct a condition.
     *
     * @param duration value to compare activity durations to
     */
    public HasDurationShorterThan(Duration duration) {
        this.duration = duration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean appliesTo(List<ActivityDuration> activityDurations) {
        for (ActivityDuration activityDuration: activityDurations) {
            if (duration.compareTo(activityDuration.getDuration()) > 0) {
                return true;
            }
        }
        return false;
    }

}
