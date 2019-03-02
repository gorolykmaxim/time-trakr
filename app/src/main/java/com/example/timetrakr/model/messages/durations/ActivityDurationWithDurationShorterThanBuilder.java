package com.example.timetrakr.model.messages.durations;

import com.example.timetrakr.common.DurationFormatter;
import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.example.timetrakr.model.messages.PartBuilder;

import java.time.Duration;
import java.util.List;

/**
 * Finds a duration of a activity, duration of which is shorter than the specified value. Use this builder
 * in pair with conditions like {@link HasDurationShorterThan}.
 */
public class ActivityDurationWithDurationShorterThanBuilder implements PartBuilder<List<ActivityDuration>> {

    private Duration duration;
    private DurationFormatter formatter;

    /**
     * Construct a builder.
     *
     * @param duration duration to compare activity durations to
     * @param formatter formatter, used to properly format the duration, before building the part
     */
    public ActivityDurationWithDurationShorterThanBuilder(Duration duration, DurationFormatter formatter) {
        this.duration = duration;
        this.formatter = formatter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String buildFrom(List<ActivityDuration> activityDurations) {
        for (ActivityDuration activityDuration: activityDurations) {
            if (duration.compareTo(activityDuration.getDuration()) > 0) {
                return formatter.format(activityDuration.getDuration());
            }
        }
        throw new NoActivityShorterThanError(duration);
    }

}
