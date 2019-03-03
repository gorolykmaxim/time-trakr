package com.example.timetrakr.viewmodel.activities.started;

import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.model.activity.events.ActivityStartEventFactory;
import com.example.timetrakr.model.activity.events.ActivityStartEventRepository;
import com.example.timetrakr.model.activity.events.AnotherActivityAlreadyStartedException;
import com.example.timetrakr.model.activity.events.NewActivityNameIsTooShortException;
import com.example.timetrakr.model.activity.events.StartActivityInFutureException;

import java.time.LocalDateTime;

import androidx.lifecycle.MutableLiveData;

/**
 * Runnable, that starts new activity with the specified name at the specified date.
 */
public class StartNewActivity implements Runnable {

    private ActivityStartEventFactory factory;
    private ActivityStartEventRepository repository;
    private String activityName;
    private LocalDateTime startDate;
    private MutableLiveData<NewActivityNameIsTooShortException> nameIsTooShortObservable;
    private MutableLiveData<AnotherActivityAlreadyStartedException> activityAlreadyStartedObservable;
    private MutableLiveData<StartActivityInFutureException> startInFutureObservable;

    /**
     * Construct activity start operation.
     *
     * @param factory factory used to create new activity start event
     * @param repository repository used to save newly created activity start event
     * @param activityName name of the started activity
     * @param startDate time when activity has been started
     * @param nameIsTooShortObservable observable to notify about {@link NewActivityNameIsTooShortException} happened
     * @param activityAlreadyStartedObservable observable to notify about {@link AnotherActivityAlreadyStartedException} happened
     * @param startInFutureObservable observable to notify about {@link StartActivityInFutureException} happened
     */
    public StartNewActivity(ActivityStartEventFactory factory, ActivityStartEventRepository repository,
                            String activityName, LocalDateTime startDate,
                            MutableLiveData<NewActivityNameIsTooShortException> nameIsTooShortObservable,
                            MutableLiveData<AnotherActivityAlreadyStartedException> activityAlreadyStartedObservable,
                            MutableLiveData<StartActivityInFutureException> startInFutureObservable) {
        this.factory = factory;
        this.repository = repository;
        this.activityName = activityName;
        this.startDate = startDate;
        this.nameIsTooShortObservable = nameIsTooShortObservable;
        this.activityAlreadyStartedObservable = activityAlreadyStartedObservable;
        this.startInFutureObservable = startInFutureObservable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            ActivityStartEvent activityStartEvent = factory.createNew(activityName, startDate);
            repository.save(activityStartEvent);
        } catch (NewActivityNameIsTooShortException e) {
            nameIsTooShortObservable.postValue(e);
        } catch (AnotherActivityAlreadyStartedException e) {
            activityAlreadyStartedObservable.postValue(e);
        } catch (StartActivityInFutureException e) {
            startInFutureObservable.postValue(e);
        }
    }
}
