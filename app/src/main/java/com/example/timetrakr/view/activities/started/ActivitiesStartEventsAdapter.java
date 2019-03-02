package com.example.timetrakr.view.activities.started;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.timetrakr.R;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.view.common.recycler.ListAdapter;
import com.example.timetrakr.view.common.recycler.RecyclerViewAdapterStrategy;
import com.example.timetrakr.view.common.recycler.SingleDifferenceFindingStrategy;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Adapter used to display information about activity start events in the recycler view.
 */
public class ActivitiesStartEventsAdapter extends ListAdapter<ActivityStartEvent, ActivityStartViewHolder> {

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
     * {@inheritDoc}
     */
    @Override
    public void displayList(List<ActivityStartEvent> activityStartEvents) {
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
