package com.example.timetrakr.model.activity.duration;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Selection of several activity durations.
 *
 * <p>Add activity durations to the selection to calculate their total duration. You can observe
 * changes of total duration while adding and removing activity durations from selection. You can
 * observe changes done to the actual selected activity list as well.</p>
 *
 * <p>At the same time selection can listen to changes in the specified activity durations list
 * and update current total duration accordingly. In case even some of selected durations will be
 * present in the list, total duration will be calculated for them only.</p>
 *
 * <p>Selection is stateful, so in case you want to use existing selection in a different place
 * and context, clear it first.</p>
 *
 */
public class ActivityDurationSelection {

    private MutableLiveData<Set<String>> observableSelectedActivities;
    private MediatorLiveData<Duration> observableTotalDuration;

    /**
     * Create a selection.
     *
     * @param observableActivityDurations activity duration list to listen for duration changes to
     */
    public ActivityDurationSelection(LiveData<List<ActivityDuration>> observableActivityDurations) {
        observableSelectedActivities = new MutableLiveData<>();
        observableTotalDuration = new MediatorLiveData<>();
        observableTotalDuration.addSource(observableActivityDurations, activityDurations -> observableTotalDuration.setValue(calculateTotalDurationFor(activityDurations)));
    }

    /**
     * Calculate total duration for all activities in the specified list, which currently
     * belong to this selection.
     *
     * @param activityDurations activity durations to calculate total duration for
     * @return total duration of all activities from the specified list, that belong to this selection
     */
    public Duration calculateTotalDurationFor(List<ActivityDuration> activityDurations) {
        Duration totalDuration = Duration.ZERO;
        Set<String> selectedActivities = observableSelectedActivities.getValue();
        if (selectedActivities != null) {
            for (ActivityDuration activityDuration: activityDurations) {
                if (selectedActivities.contains(activityDuration.getActivityName())) {
                    totalDuration = totalDuration.plus(activityDuration.getDuration());
                }
            }
        }
        return totalDuration;
    }

    /**
     * Add activity duration to the selection. In case activity already belong to this selection
     * the method call will be ignored.
     *
     * @param activityDuration activity duration to add
     */
    public void add(ActivityDuration activityDuration) {
        Set<String> selectedActivities = observableSelectedActivities.getValue();
        if (selectedActivities == null) {
            selectedActivities = new HashSet<>();
        }
        if (!selectedActivities.contains(activityDuration.getActivityName())) {
            selectedActivities.add(activityDuration.getActivityName());
            observableSelectedActivities.setValue(selectedActivities);
            Duration totalDuration = observableTotalDuration.getValue();
            if (totalDuration == null) {
                totalDuration = activityDuration.getDuration();
            } else {
                totalDuration = totalDuration.plus(activityDuration.getDuration());
            }
            observableTotalDuration.setValue(totalDuration);
        }
    }

    /**
     * Remove activity duration from the selection. In case activity does not belong to this
     * selection the method call will be ignored.
     *
     * @param activityDuration activity duration to remove
     */
    public void remove(ActivityDuration activityDuration) {
        Set<String> selectedActivities = observableSelectedActivities.getValue();
        if (selectedActivities != null && selectedActivities.contains(activityDuration.getActivityName())) {
            selectedActivities.remove(activityDuration.getActivityName());
            observableSelectedActivities.setValue(selectedActivities);
            Duration totalDuration = observableTotalDuration.getValue();
            if (totalDuration != null) {
                totalDuration = totalDuration.minus(activityDuration.getDuration());
                observableTotalDuration.setValue(totalDuration);
            }
        }
    }

    /**
     * Clear the selection, removing all selected activities and zeroing the total duration.
     */
    public void clear() {
        observableTotalDuration.setValue(Duration.ZERO);
        observableSelectedActivities.setValue(new HashSet<>());
    }

    /**
     * Get observable for a total duration of activities, that belong to this selection.
     *
     * @return observable of a total duration
     */
    public LiveData<Duration> getObservableTotalDuration() {
        return observableTotalDuration;
    }

    /**
     * Get observable of a set of activities, that belong to this set.
     *
     * @return observable of a set of activities
     */
    public LiveData<Set<String>> getObservableSelectedActivities() {
        return observableSelectedActivities;
    }

}
