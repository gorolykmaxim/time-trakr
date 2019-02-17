package com.example.timetrakr.view.activities.started;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.timetrakr.R;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.view.common.recycler.RecyclerViewAdapterStrategy;
import com.example.timetrakr.view.common.recycler.SingleDifferenceFindingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter used to display information about activity start events in the recycler view.
 */
public class ActivitiesStartEventsAdapter extends RecyclerView.Adapter<ActivityStartViewHolder> {

    private List<ActivityStartEvent> activityStartEvents;
    private RecyclerViewAdapterStrategy strategy;
    private ActivityStartViewHolderFactory factory;

    /**
     * Construct adapter.
     *
     * @param factory factory used to create view holder instances
     */
    public ActivitiesStartEventsAdapter(ActivityStartViewHolderFactory factory) {
        activityStartEvents = new ArrayList<>();
        strategy = new SingleDifferenceFindingStrategy();
        this.factory = factory;
    }

    /**
     * Specify strategy, used by the adapter to determine which items of the recycler view
     * have changed and should be updated. Default strategy is {@link SingleDifferenceFindingStrategy}.
     *
     * @param strategy strategy to use
     */
    public void setStrategy(RecyclerViewAdapterStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Update data set of the adapter with specified list of activity start events.
     *
     * @param activityStartEvents list of activity start events to display in the recycler view
     */
    public void updateActivityStartEvents(List<ActivityStartEvent> activityStartEvents) {
        List<ActivityStartEvent> oldActivityStartEvents = this.activityStartEvents;
        this.activityStartEvents = activityStartEvents;
        strategy.notifyDataSetChanged(this, oldActivityStartEvents, activityStartEvents);
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public ActivityStartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewGroup activityView = (ViewGroup) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.deletable_activity_view, viewGroup, false);
        return factory.create(activityView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(@NonNull ActivityStartViewHolder viewHolder, int i) {
        ActivityStartEvent activityStartEvent = activityStartEvents.get(i);
        viewHolder.update(activityStartEvent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return activityStartEvents.size();
    }

}
