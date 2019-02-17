package com.example.timetrakr.model.activity.duration;

import com.example.timetrakr.model.activity.events.ActivityStartEvent;

import java.time.Duration;

/**
 * Factory of {@link ActivityDuration} instances.
 */
public class ActivityDurationFactory {

    /**
     * Calculate duration of the first specified activity by time interval between the first
     * specified activity and the second one.
     *
     * @param event start event of the activity, duration for which should be calculated
     * @param nextEvent event that follows the previously specified event
     * @return duration of the activity of the first specified event
     */
    public ActivityDuration createFromTwoSequentialEvents(ActivityStartEvent event, ActivityStartEvent nextEvent) {
        Duration duration = Duration.between(event.getStartDate(), nextEvent.getStartDate());
        return new ActivityDuration(event.getActivityName(), duration);
    }

}
