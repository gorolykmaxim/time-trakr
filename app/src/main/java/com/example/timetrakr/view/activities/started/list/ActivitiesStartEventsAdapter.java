package com.example.timetrakr.view.activities.started.list;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.view.common.recycler.ListAdapter;
import com.example.timetrakr.view.common.recycler.RecyclerViewAdapterStrategy;
import com.example.timetrakr.view.common.recycler.SingleDifferenceFindingStrategy;
import com.gorolykmaxim.android.commons.recyclerview.GenericViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter used to display information about activity start events in the recycler view.
 */
public class ActivitiesStartEventsAdapter extends ListAdapter<ActivityStartEvent, GenericViewHolder<StartedActivityCard>> {

    private List<ActivityStartEvent> activityStartEvents;
    private RecyclerViewAdapterStrategy strategy;

    /**
     * Construct adapter.
     */
    public ActivitiesStartEventsAdapter() {
        activityStartEvents = new ArrayList<>();
        strategy = new SingleDifferenceFindingStrategy();
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
    public GenericViewHolder<StartedActivityCard> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GenericViewHolder<>(new StartedActivityCard(viewGroup.getContext()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder<StartedActivityCard> viewHolder, int i) {
        ActivityStartEvent activityStartEvent = activityStartEvents.get(i);
        viewHolder.getView().setActivityStartEvent(activityStartEvent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemCount() {
        return activityStartEvents.size();
    }

}
