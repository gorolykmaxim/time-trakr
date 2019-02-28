package com.example.timetrakr.viewmodel.activities.started;

import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.model.activity.events.ActivityStartEventFactory;
import com.example.timetrakr.model.activity.events.ActivityStartEventRepository;

import java.util.function.Supplier;

/**
 * Runnable, that erases activity start event with the specified name and start date.
 */
public class DeleteActivityStartEvent implements Runnable {

    private ActivityStartEventFactory factory;
    private ActivityStartEventRepository repository;
    private String activityName, activityStartDate;

    /**
     * Construct activity start event removal operation.
     *
     * @param factory factory used to recreate instance of the activity that will be deleted
     * @param repository repository used to delete the activity start event
     * @param activityName name of the activity start event that should be deleted
     * @param activityStartDate date, when the event have been created
     */
    public DeleteActivityStartEvent(ActivityStartEventFactory factory, ActivityStartEventRepository repository, String activityName, String activityStartDate) {
        this.factory = factory;
        this.repository = repository;
        this.activityName = activityName;
        this.activityStartDate = activityStartDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        ActivityStartEvent activityStartEvent = factory.recreateFrom(activityName, activityStartDate);
        repository.delete(activityStartEvent);
    }
}
