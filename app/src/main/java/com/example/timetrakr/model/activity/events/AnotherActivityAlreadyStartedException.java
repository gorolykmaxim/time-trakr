package com.example.timetrakr.model.activity.events;

/**
 * User tried to start an activity at the date-time when another activity has been already started.
 */
public class AnotherActivityAlreadyStartedException extends Exception {

    private ActivityStartEvent anotherActivity;

    /**
     * Construct the exception.
     *
     * @param anotherActivity activity that was previously started at that time.
     */
    public AnotherActivityAlreadyStartedException(ActivityStartEvent anotherActivity) {
        super(String.format("'%s' has already been started at %s", anotherActivity.getActivityName(), anotherActivity.getStartDate()));
        this.anotherActivity = anotherActivity;
    }

    /**
     * Get activity start event of the activity, that was previously started at that time.
     *
     * @return already started activity start event
     */
    public ActivityStartEvent getAnotherActivity() {
        return anotherActivity;
    }
}
