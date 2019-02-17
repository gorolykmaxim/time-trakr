package com.example.timetrakr.model.activity.events;

/**
 * Failed to create a new activity start event because activity name is too short.
 */
public class NewActivityNameIsTooShortException extends Exception {

    private String activityName;
    private int expectedMinimalLength;

    /**
     * Construct an exception.
     *
     * @param activityName name of the activity
     * @param expectedMinimalLength expected minimal length of an activity name
     */
    public NewActivityNameIsTooShortException(String activityName, int expectedMinimalLength) {
        super(String.format("Activity name '%s' is too short. Use activity name with at least %s characters.", activityName, expectedMinimalLength));
        this.activityName = activityName;
        this.expectedMinimalLength = expectedMinimalLength;
    }

    /**
     * Return name of the activity, creation of which has failed.
     *
     * @return name of the activity
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * Return expected minimal length of the activity name
     *
     * @return minimal activity name length
     */
    public int getExpectedMinimalLength() {
        return expectedMinimalLength;
    }
}
