package com.example.timetrakr.view.activities.durations;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.timetrakr.R;
import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.example.timetrakr.view.common.recycler.RecyclerViewAdapterStrategy;
import com.example.timetrakr.view.common.recycler.SimpleStrategy;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter used to display information about durations of activities in a recycler view.
 *
 * <p>Adapter only displays information about activities, durations of which are longer
 * than 1 minute. It is done, because in terms of user interest activities with durations
 * shorter than one minute are not important.</p>
 *
 */
public class ActivitiesDurationsAdapter extends RecyclerView.Adapter<ActivityDurationViewHolder> {

    private List<ActivityDuration> activityDurations;
    private RecyclerViewAdapterStrategy strategy;
    private ActivityDurationViewHolderFactory factory;

    /**
     * Construct an adapter.
     *
     * @param factory factory used to create view holders for element of the recycler view.
     */
    public ActivitiesDurationsAdapter(ActivityDurationViewHolderFactory factory) {
        activityDurations = new ArrayList<>();
        strategy = new SimpleStrategy();
        this.factory = factory;
    }

    /**
     * Set strategy, used to notify observers of adapter about changes in list of activity
     * durations, that should be displayed in recycler view.
     * Default strategy is {@link SimpleStrategy}.
     *
     * @param strategy strategy to use
     */
    public void setStrategy(RecyclerViewAdapterStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Update list of activity durations that should be displayed in the recycler view.
     *
     * @param activityDurations new activity durations to display
     */
    public void updateActivityDurations(List<ActivityDuration> activityDurations) {
        List<ActivityDuration> oldActivityDurations = this.activityDurations;
        // Show only activities which took more than one minute to complete, because
        // shorted ones are not important.
        Duration oneMinute = Duration.ofMinutes(1);
        activityDurations = activityDurations.stream()
                .filter(a -> a.getDuration().compareTo(oneMinute) >= 0)
                .collect(Collectors.toList());
        this.activityDurations = activityDurations;
        strategy.notifyDataSetChanged(this, oldActivityDurations, activityDurations);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ActivityDurationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewGroup activityView = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_view, viewGroup, false);
        return factory.create(activityView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(@NonNull ActivityDurationViewHolder viewHolder, int i) {
        ActivityDuration activityDuration = activityDurations.get(i);
        viewHolder.update(activityDuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return activityDurations.size();
    }

}
