package com.example.timetrakr.viewmodel.activities.started;

import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.model.activity.events.ActivityStartEventFactory;
import com.example.timetrakr.model.activity.events.ActivityStartEventRepository;
import com.example.timetrakr.model.activity.events.NewActivityNameIsTooShortException;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * Operation of activity start, that is performed in {@link android.os.AsyncTask}.
 */
public class StartNewActivity implements Supplier<NewActivityNameIsTooShortException> {

    private ActivityStartEventFactory factory;
    private ActivityStartEventRepository repository;
    private String activityName;
    private LocalDateTime startDate;

    /**
     * Construct activity start operation.
     *
     * @param factory factory used to create new activity start event
     * @param repository repository used to save newly created activity start event
     * @param activityName name of the started activity
     * @param startDate time when activity has been started
     */
    public StartNewActivity(ActivityStartEventFactory factory, ActivityStartEventRepository repository, String activityName, LocalDateTime startDate) {
        this.factory = factory;
        this.repository = repository;
        this.activityName = activityName;
        this.startDate = startDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NewActivityNameIsTooShortException get() {
        try {
            ActivityStartEvent activityStartEvent = factory.createNew(activityName, startDate);
            repository.save(activityStartEvent);
            return null;
        } catch (NewActivityNameIsTooShortException e) {
            return e;
        }
    }
}
