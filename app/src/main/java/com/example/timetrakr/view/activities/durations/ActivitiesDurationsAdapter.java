package com.example.timetrakr.view.activities.durations;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.example.timetrakr.R;
import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.gorolykmaxim.android.commons.recyclerview.ImmutableItemDiffCallback;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adapter used to display information about durations of activities in a recycler view.
 *
 * <p>Adapter only displays information about activities, durations of which are longer
 * than 1 minute. It is done, because in terms of user interest activities with durations
 * shorter than one minute are not important.</p>
 *
 */
public class ActivitiesDurationsAdapter extends ListAdapter<ActivityDuration, ActivityDurationViewHolder> {

    private Set<String> selectedActivities;
    private ActivityDurationViewHolderFactory factory;
    private OnItemSelectListener onItemSelectListener;

    /**
     * Construct an adapter.
     *
     * @param factory factory used to create view holders for element of the recycler view.
     */
    public ActivitiesDurationsAdapter(ActivityDurationViewHolderFactory factory) {
        super(new ImmutableItemDiffCallback());
        selectedActivities = new HashSet<>();
        this.factory = factory;
    }

    /**
     * Specify a listener that will be called when an item gets selected/de-selected.
     *
     * @param onItemSelectListener listener to call
     */
    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    /**
     * Update list of activity durations that should be displayed in the recycler view.
     *
     * @param activityDurations new activity durations to display
     */
    public void updateActivityDurations(List<ActivityDuration> activityDurations) {
        // Show only activities which took more than one minute to complete, because
        // shorted ones are not important.
        Duration oneMinute = Duration.ofMinutes(1);
        activityDurations = activityDurations.stream()
                .filter(a -> a.getDuration().compareTo(oneMinute) >= 0)
                .collect(Collectors.toList());
        submitList(activityDurations);
    }

    /**
     * Update list of activities, that should be displayed as selected.
     *
     * @param selectedActivityDurations list of selected activities
     */
    public void updateSelectedActivityDurations(Set<String> selectedActivityDurations) {
        this.selectedActivities = selectedActivityDurations;
        notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ActivityDurationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewGroup activityView = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_view, viewGroup, false);
        ActivityDurationViewHolder viewHolder = factory.create(activityView);
        viewHolder.setOnClickListener(v -> {
            if (onItemSelectListener != null) {
                onItemSelectListener.onSelect(viewHolder, selectedActivities.contains(viewHolder.getActivityDuration().getActivityName()));
            }
        });
        return viewHolder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(@NonNull ActivityDurationViewHolder viewHolder, int i) {
        ActivityDuration activityDuration = getItem(i);
        viewHolder.update(activityDuration);
        if (selectedActivities.contains(activityDuration.getActivityName())) {
            viewHolder.select();
        } else {
            viewHolder.deselect();
        }
    }

    /**
     * Listener of a adapter item selection event.
     */
    public interface OnItemSelectListener {

        /**
         * Called when an item gets selected/de-selected.
         *
         * @param viewHolder selected view holder
         * @param isSelected true if view holder is currently selected
         */
        void onSelect(ActivityDurationViewHolder viewHolder, boolean isSelected);
    }

}
