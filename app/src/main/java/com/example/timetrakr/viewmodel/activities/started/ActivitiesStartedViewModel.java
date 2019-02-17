package com.example.timetrakr.viewmodel.activities.started;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.example.timetrakr.TimeTrakrApplication;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.model.activity.events.ActivityStartEventFactory;
import com.example.timetrakr.model.activity.events.ActivityStartEventRepository;
import com.example.timetrakr.viewmodel.common.AsyncTaskExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * View model of the view that shows today's  activity start events and allows creating new
 * and deleting old ones.
 */
public class ActivitiesStartedViewModel extends AndroidViewModel {

    private LiveData<List<ActivityStartEvent>> activityStartEvents;
    private ActivityStartEventFactory factory;
    private ActivityStartEventRepository repository;
    private AsyncTaskExecutor executor;

    /**
     * Construct view model.
     *
     * @param application reference to the application instance
     */
    public ActivitiesStartedViewModel(@NonNull Application application) {
        super(application);
        TimeTrakrApplication timeTrakrApplication = (TimeTrakrApplication)application;
        repository = timeTrakrApplication.getActivityStartEventRepository();
        executor = timeTrakrApplication.getExecutor();
        activityStartEvents = repository.getObservableForAllForToday();
        factory = timeTrakrApplication.getActivityStartEventFactory();
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
     * Create new activity start event.
     *
     * @param activityName name of the activity user started doing
     * @param startDate time when user started the activity
     * @param listener listener that will be notified in case name of the activity is shorter
     *                 than required
     */
    public void startNewActivity(String activityName, LocalDateTime startDate, Consumer listener) {
        executor.execute(new StartNewActivity(factory, repository, activityName, startDate), listener);
    }

    /**
     * Delete activity start event by the name of the activity and event's creation date.
     *
     * @param activityName name of the activity
     * @param activityStartDate event's creation date
     */
    public void deleteActivityStart(String activityName, String activityStartDate) {
        executor.execute(new DeleteActivityStartEvent(factory, repository, activityName, activityStartDate));
    }

}
