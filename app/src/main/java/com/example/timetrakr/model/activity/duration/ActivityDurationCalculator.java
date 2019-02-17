package com.example.timetrakr.model.activity.duration;

import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.model.activity.events.ActivityStartEventFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Calculates durations for specified ordered sequence of activity start events.
 */
public class ActivityDurationCalculator {

    private ActivityDurationFactory durationFactory;
    private ActivityStartEventFactory startEventFactory;

    /**
     * Construct the calculator.
     *
     * @param durationFactory factory used to calculate activity durations between two sequential
     *                        activity start events
     * @param startEventFactory factory used to create artificial events, required by the algorithm
     */
    public ActivityDurationCalculator(ActivityDurationFactory durationFactory, ActivityStartEventFactory startEventFactory) {
        this.durationFactory = durationFactory;
        this.startEventFactory = startEventFactory;
    }

    /**
     * Calculate durations for specified collection of activities.
     *
     * <p>Specified activity start events have be ordered by their start date-time, so they are
     * sequentially following one another from the first activity start event to the last one.</p>
     *
     * @param activityStartEvents sequentially ordered list of activity start events,
     *                            durations of which should be calculated
     * @return collection of durations of activities, found in the specified activity start event
     * list, that preserves the order in which activities occurred
     */
    public List<ActivityDuration> calculateDurationsFromEvents(List<ActivityStartEvent> activityStartEvents) {
        // We will use this to calculate a name, that does not belong to any
        // of the user-registered activities.
        Set<String> activitiesFound = new HashSet<>();
        Map<String, ActivityDuration> activityNameToActivityDurationMap = new TreeMap<>();
        for (int i = 0; i < activityStartEvents.size(); i++) {
            ActivityStartEvent event = activityStartEvents.get(i);
            String activityName = event.getActivityName();
            activitiesFound.add(activityName);
            ActivityStartEvent nextEvent;
            if (i + 1 < activityStartEvents.size()) {
                // We are processing event that is not the last, so we will use the start time
                // of the following activity as the end time of the current one.
                nextEvent = activityStartEvents.get(i + 1);
            } else {
                // We are processing the last event, registered by the user.
                // We need to createNew an artificial activity start event with a name,
                // that does not belong to any of today's registered activities.
                // This event is needed to calculate time interval between the last
                // user-registered event and now.
                nextEvent = startEventFactory.recreateFrom(activitiesFound.toString());
            }
            ActivityDuration currentActivityDuration = durationFactory.createFromTwoSequentialEvents(event, nextEvent);
            ActivityDuration existingActivityDuration = activityNameToActivityDurationMap.get(activityName);
            if (existingActivityDuration == null) {
                // This is the first time we are calculating duration of an activity
                // with such a name.
                existingActivityDuration = currentActivityDuration;
            } else {
                // We've already calculated duration for such an activity at least once.
                // This means that this activity was interrupted by another one.
                // Still we need to calculate total duration of this activity for today.
                existingActivityDuration = existingActivityDuration.prolongBy(currentActivityDuration.getDuration());
            }
            activityNameToActivityDurationMap.put(activityName, existingActivityDuration);
        }
        return new ArrayList<>(activityNameToActivityDurationMap.values());
    }

}
