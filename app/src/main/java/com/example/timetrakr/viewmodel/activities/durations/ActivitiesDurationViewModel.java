package com.example.timetrakr.viewmodel.activities.durations;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.timetrakr.TimeTrakrApplication;
import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.example.timetrakr.model.activity.duration.ActivityDurationCalculator;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.model.activity.events.ActivityStartEventRepository;

import java.util.List;

/**
 * View model of the view that shows durations of activities, done today.
 */
public class ActivitiesDurationViewModel extends AndroidViewModel {

    private MediatorLiveData<List<ActivityDuration>> activityDurations;
    private LiveData<List<ActivityStartEvent>> activityStartEvents;
    private ActivityDurationCalculator durationCalculator;

    /**
     * Construct the view model.
     *
     * @param application reference to the application instance
     */
    public ActivitiesDurationViewModel(@NonNull Application application) {
        super(application);
        TimeTrakrApplication timeTrakrApplication = (TimeTrakrApplication)application;
        ActivityStartEventRepository repository = timeTrakrApplication.getActivityStartEventRepository();
        durationCalculator = timeTrakrApplication.getActivityDurationCalculator();
        activityStartEvents = repository.getObservableForAllForToday();
        activityDurations = new MediatorLiveData<>();
        activityDurations.addSource(activityStartEvents, this::triggerActivityDurationCalculationFor);
    }

    /**
     * Get observable for list of durations of today's activities.
     *
     * @return observable for list of today's activity durations
     */
    public LiveData<List<ActivityDuration>> getActivityDurations() {
        return activityDurations;
    }

    /**
     * Force recalculation of durations of today's activities.
     */
    public void recalculateActivityDurations() {
        triggerActivityDurationCalculationFor(activityStartEvents.getValue());
    }

    /**
     * Calculate durations for activities from the specified list of activity start events and
     * notify observers of activity durations observable about the results.
     *
     * @param activityStartEvents list of activity start events to calculate durations for.
     *                            In case null is passed - the call will be ignored.
     */
    private void triggerActivityDurationCalculationFor(@Nullable List<ActivityStartEvent> activityStartEvents) {
        if (activityStartEvents != null) {
            activityDurations.setValue(durationCalculator.calculateDurationsFromEvents(activityStartEvents));
        }
    }

}
