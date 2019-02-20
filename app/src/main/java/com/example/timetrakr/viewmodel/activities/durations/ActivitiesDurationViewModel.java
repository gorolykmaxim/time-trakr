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
import com.example.timetrakr.model.activity.duration.ActivityDurationSelection;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.model.activity.events.ActivityStartEventRepository;

import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * View model of the view that shows durations of activities, done today.
 */
public class ActivitiesDurationViewModel extends AndroidViewModel {

    private MediatorLiveData<List<ActivityDuration>> observableActivityDurations;
    private LiveData<List<ActivityStartEvent>> activityStartEvents;
    private ActivityDurationCalculator durationCalculator;
    private ActivityDurationSelection activityDurationSelection;

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
        observableActivityDurations = new MediatorLiveData<>();
        observableActivityDurations.addSource(activityStartEvents, this::triggerActivityDurationCalculationFor);
        activityDurationSelection = new ActivityDurationSelection(observableActivityDurations);
    }

    /**
     * Specify activity duration selection of this view model. The view model will use this
     * selection to store selected activity durations and to display total duration of
     * stored activities.
     *
     * @param activityDurationSelection selection to use by view model
     */
    public void setActivityDurationSelection(ActivityDurationSelection activityDurationSelection) {
        this.activityDurationSelection = activityDurationSelection;
    }

    /**
     * Add specified activity duration to the current selection. In case activity already belongs
     * to the selection, the method call will be ignored.
     *
     * @param activityDuration activity duration to add to the selection
     */
    public void selectActivityDuration(ActivityDuration activityDuration) {
        activityDurationSelection.add(activityDuration);
    }

    /**
     * Remove specified activity duration from the current selection. In case activity does not
     * belong to the selection the method call will be ignored.
     *
     * @param activityDuration activity duration to remove from the selection
     */
    public void deselectActivityDuration(ActivityDuration activityDuration) {
        activityDurationSelection.remove(activityDuration);
    }

    /**
     * Clear current selection of activity durations.
     */
    public void clearSelectedActivityDurations() {
        activityDurationSelection.clear();
    }

    /**
     * Get observable of total duration of selected activities.
     *
     * @return observable of total duration
     */
    public LiveData<Duration> getObservableTotalDuration() {
        return activityDurationSelection.getObservableTotalDuration();
    }

    /**
     * Get observable of a set of selected activities.
     *
     * @return observable of selected activities
     */
    public LiveData<Set<String>> getObservableSelectedActivities() {
        return activityDurationSelection.getObservableSelectedActivities();
    }

    /**
     * Get observable for list of durations of today's activities.
     *
     * @return observable for list of today's activity durations
     */
    public LiveData<List<ActivityDuration>> getActivityDurations() {
        return observableActivityDurations;
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
            observableActivityDurations.setValue(durationCalculator.calculateDurationsFromEvents(activityStartEvents));
        }
    }

}
