package com.example.timetrakr.viewmodel.activities.started;

import android.app.Application;

import com.example.timetrakr.TimeTrakrApplication;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.model.activity.events.ActivityStartEventFactory;
import com.example.timetrakr.model.activity.events.ActivityStartEventRepository;
import com.example.timetrakr.model.activity.events.AnotherActivityAlreadyStartedException;
import com.example.timetrakr.model.activity.events.NewActivityNameIsTooShortException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * View model of the view that shows today's  activity start events and allows creating new
 * and deleting old ones.
 */
public class ActivitiesStartedViewModel extends AndroidViewModel {

    private LiveData<List<ActivityStartEvent>> activityStartEvents;
    private MutableLiveData<NewActivityNameIsTooShortException> nameIsTooShortObservable;
    private MutableLiveData<AnotherActivityAlreadyStartedException> activityAlreadyStartedObservable;
    private ActivityStartEventFactory factory;
    private ActivityStartEventRepository repository;
    private ExecutorService executorService;

    /**
     * Construct view model.
     *
     * @param application reference to the application instance
     */
    public ActivitiesStartedViewModel(@NonNull Application application) {
        super(application);
        TimeTrakrApplication timeTrakrApplication = (TimeTrakrApplication)application;
        repository = timeTrakrApplication.getActivityStartEventRepository();
        executorService = timeTrakrApplication.getExecutorService();
        activityStartEvents = repository.getObservableForAllForToday();
        factory = timeTrakrApplication.getActivityStartEventFactory();
        nameIsTooShortObservable = new MutableLiveData<>();
        activityAlreadyStartedObservable = new MutableLiveData<>();
    }

    /**
     * Get observable for list of activity start events occurred today.
     *
     * @return observable for list of today's activity start events
     */
    public LiveData<List<ActivityStartEvent>> getActivityStartEvents() {
        return activityStartEvents;
    }

    /**
     * Get list of today's activity start events, with name similar to the specified one.
     *
     * @param activityName activity name to use as a search term
     * @return list of start events of activities that have similar name
     */
    public List<ActivityStartEvent> getActivitiesWithSimilarNames(String activityName) {
        return repository.findAllForTodayWithNameLike(activityName);
    }

    /**
     * Get observable for an exception, that may happen if user will try to start an activity
     * with a name that is too short.
     *
     * @return observable for a too short activity name exception
     */
    public LiveData<NewActivityNameIsTooShortException> getNameIsTooShortObservable() {
        return nameIsTooShortObservable;
    }

    /**
     * Get observable for an exception, that may happen if user will try to start an activity
     * at the date when another activity has been started.
     *
     * @return observable for an activity start date conflict exception
     */
    public LiveData<AnotherActivityAlreadyStartedException> getActivityAlreadyStartedObservable() {
        return activityAlreadyStartedObservable;
    }

    /**
     * Create new activity start event.
     *
     * @param activityName name of the activity user started doing
     * @param startDate time when user started the activity
     */
    public void startNewActivity(String activityName, LocalDateTime startDate) {
        executorService.execute(new StartNewActivity(factory, repository, activityName, startDate,
                nameIsTooShortObservable,
                activityAlreadyStartedObservable));
    }

    /**
     * Delete activity start event by the name of the activity and event's creation date.
     *
     * @param activityName name of the activity
     * @param activityStartDate event's creation date
     */
    public void deleteActivityStart(String activityName, String activityStartDate) {
        executorService.execute(new DeleteActivityStartEvent(factory, repository, activityName, activityStartDate));
    }

}
