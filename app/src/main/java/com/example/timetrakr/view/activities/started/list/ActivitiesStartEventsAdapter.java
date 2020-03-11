package com.example.timetrakr.view.activities.started.list;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.view.common.recycler.ListAdapter;
import com.gorolykmaxim.android.commons.recyclerview.GenericViewHolder;
import com.gorolykmaxim.android.commons.recyclerview.ImmutableItemDiffCallback;

import java.util.List;

/**
 * Adapter used to display information about activity start events in the recycler view.
 */
public class ActivitiesStartEventsAdapter extends ListAdapter<ActivityStartEvent, GenericViewHolder<StartedActivityCard>> {
    /**
     * Construct adapter.
     */
    public ActivitiesStartEventsAdapter() {
        super(new ImmutableItemDiffCallback());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayList(List<ActivityStartEvent> activityStartEvents) {
        submitList(activityStartEvents);
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
        viewHolder.getView().setActivityStartEvent(getItem(i));
    }
}
