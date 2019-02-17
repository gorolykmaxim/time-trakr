package com.example.timetrakr.model.activity.duration;

import java.time.Duration;
import java.util.Objects;

/**
 * Duration of a specific activity.
 *
 * <p>{@link ActivityDuration} is immutable, meaning any modification creates a new instance.</p>
 *
 */
public class ActivityDuration {

    private String activityName;
    private Duration duration;

    /**
     * Construct activity duration.
     *
     * @param activityName name of the activity
     * @param duration duration of the activity
     */
    public ActivityDuration(String activityName, Duration duration) {
        this.activityName = activityName;
        this.duration = duration;
    }

    /**
     * Prolong this activity duration by the specified amount.
     *
     * @param duration duration by which this activity's duration should be increased
     * @return updated activity duration instance
     */
    public ActivityDuration prolongBy(Duration duration) {
        Duration updatedDuration = this.duration.plus(duration);
        return new ActivityDuration(activityName, updatedDuration);
    }

    /**
     * Get name of the activity
     *
     * @return name of the activity
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * Get duration of this activity
     *
     * @return duration of this activity
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityDuration that = (ActivityDuration) o;
        return Objects.equals(activityName, that.activityName) &&
                Objects.equals(duration, that.duration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(activityName, duration);
    }
}
