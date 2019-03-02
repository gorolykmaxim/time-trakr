package com.example.timetrakr.model.messages.durations;

import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.example.timetrakr.model.messages.PartBuilder;

import java.time.Duration;
import java.util.List;

/**
 * Finds a name of a activity, duration of which is shorter than the specified value. Use this builder
 * in pair with conditions like {@link HasDurationShorterThan}.
 */
public class ActivityNameWithDurationShorterThanBuilder implements PartBuilder<List<ActivityDuration>> {

    private Duration duration;

    /**
     * Construct a builder.
     *
     * @param duration duration to compare activity durations to
     */
    public ActivityNameWithDurationShorterThanBuilder(Duration duration) {
        this.duration = duration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildFrom(List<ActivityDuration> activityDurations) {
        for (ActivityDuration activityDuration: activityDurations) {
            if (duration.compareTo(activityDuration.getDuration()) > 0) {
                return activityDuration.getActivityName();
            }
        }
        throw new NoActivityShorterThanError(duration);
    }

}
