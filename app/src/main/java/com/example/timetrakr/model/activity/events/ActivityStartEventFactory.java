package com.example.timetrakr.model.activity.events;

import java.time.LocalDateTime;

/**
 * Factory of {@link ActivityStartEvent} instances.
 */
public class ActivityStartEventFactory {

    private int minimalActivityNameLength;

    /**
     * Construct the factory.
     */
    public ActivityStartEventFactory() {
        minimalActivityNameLength = 3;
    }

    /**
     * Specify the minimal length for an activity name. Default value is 3 characters long.
     *
     * @param minimalActivityNameLength minimal length for an activity name
     */
    public void setMinimalActivityNameLength(int minimalActivityNameLength) {
        this.minimalActivityNameLength = minimalActivityNameLength;
    }

    /**
     * Create an event of a new activity being started by the user.
     *
     * <p>Use this method to create new activity start events.</p>
     *
     * @param activityName name of the started activity
     * @param startDate time when new activity has been started
     * @return started activity event
     * @throws NewActivityNameIsTooShortException specified activity name is shorter than
     * the specified minimal activity name length
     */
    public ActivityStartEvent createNew(String activityName, LocalDateTime startDate) throws NewActivityNameIsTooShortException {
        if (activityName.length() < minimalActivityNameLength) {
            throw new NewActivityNameIsTooShortException(activityName, minimalActivityNameLength);
        }
        return new ActivityStartEvent(activityName, startDate);
    }

    /**
     * Create an event of an activity being started.
     *
     * <p>Created activity is considered started at the point of calling this method.</p>
     *
     * <p>Use this method to create any sort of dummy event. Don't use it to create actual
     * new user-related events, since it does no activity name length validations.</p>
     *
     * @param activityName name of the started activity
     * @return started dummy activity event
     */
    public ActivityStartEvent recreateFrom(String activityName) {
        return new ActivityStartEvent(activityName, LocalDateTime.now());
    }

    /**
     * Recreate activity started event from specified name and date-time.
     *
     * @param activityName name of the recreated activity
     * @param date start date of the recreated activity
     * @return recreated activity
     */
    public ActivityStartEvent recreateFrom(String activityName, String date) {
        return new ActivityStartEvent(activityName, LocalDateTime.parse(date));
    }

}
