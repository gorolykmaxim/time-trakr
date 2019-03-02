package com.example.timetrakr.view.activities.started;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timetrakr.R;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.view.common.recycler.RecyclerViewAdapterStrategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LayoutInflater.class})
public class ActivitiesStartEventsAdapterTest {

    private DateTimeFormatter formatter;
    private ActivityStartViewHolderFactory factory;
    private ActivitiesStartEventsAdapter adapter;

    @Before
    public void setUp() throws Exception {
        formatter = DateTimeFormatter.ofPattern("HH:mm");
        factory = new ActivityStartViewHolderFactory("since %s", formatter);
        adapter = new ActivitiesStartEventsAdapter(factory);
    }

    @Test
    public void onCreateViewHolder() {
        PowerMockito.mockStatic(LayoutInflater.class);
        ViewGroup viewGroup = Mockito.mock(ViewGroup.class);
        Context context = Mockito.mock(Context.class);
        Mockito.when(viewGroup.getContext()).thenReturn(context);
        LayoutInflater inflater = Mockito.mock(LayoutInflater.class);
        ViewGroup activityView = Mockito.mock(ViewGroup.class);
        TextView primaryTextView = Mockito.mock(TextView.class);
        Mockito.when(activityView.findViewById(R.id.deletable_activity_view_primary_text)).thenReturn(primaryTextView);
        TextView secondaryTextView = Mockito.mock(TextView.class);
        Mockito.when(activityView.findViewById(R.id.deletable_activity_view_secondary_text)).thenReturn(secondaryTextView);
        Mockito.when(inflater.inflate(R.layout.deletable_activity_view, viewGroup, false)).thenReturn(activityView);
        PowerMockito.when(LayoutInflater.from(context)).thenReturn(inflater);
        // Phew, mocking is done. This is android btw. Keep that in mind. It is really hard to test.
        ActivityStartViewHolder viewHolder = adapter.onCreateViewHolder(viewGroup, 0);
        // Since we haven't updated the view holder's state yet, we expect following values to be null.
        Assert.assertNull(viewHolder.getActivityName());
        Assert.assertNull(viewHolder.getStartDate());
        ActivityStartEvent event = new ActivityStartEvent("Programming on weekends", LocalDateTime.now());
        viewHolder.update(event);
        Mockito.verify(primaryTextView).setText(event.getActivityName());
        Mockito.verify(secondaryTextView).setText(String.format("since %s", event.getStartDate().format(formatter)));
        Assert.assertEquals(event.getActivityName(), viewHolder.getActivityName());
        Assert.assertEquals(event.getStartDate(), viewHolder.getStartDate());
    }

    @Test
    public void updateDataSet() {
        // Initially we expected adapter to have no items.
        Assert.assertEquals(0, adapter.getItemCount());
        RecyclerViewAdapterStrategy strategy = Mockito.mock(RecyclerViewAdapterStrategy.class);
        adapter.setStrategy(strategy);
        ActivityStartViewHolder viewHolder = Mockito.mock(ActivityStartViewHolder.class);
        // Update adapter's data set with 3 events.
        ActivityStartEvent event1 = new ActivityStartEvent("Sleeping", LocalDateTime.now().minusHours(5));
        ActivityStartEvent event2 = new ActivityStartEvent("Eating", LocalDateTime.now().minusHours(2));
        ActivityStartEvent event3 = new ActivityStartEvent("Playing games", LocalDateTime.now());
        List<ActivityStartEvent> events = Arrays.asList(event1, event2, event3);
        adapter.displayList(events);
        Mockito.verify(strategy).notifyDataSetChanged(adapter, Collections.emptyList(), events);
        Assert.assertEquals(events.size(), adapter.getItemCount());
        onBindViewHolder(adapter, viewHolder, events);
        Mockito.reset(viewHolder);
        // Update adapter's data set with previously used data set but 1 item removed.
        List<ActivityStartEvent> updatedEvents = Arrays.asList(event1, event3);
        adapter.displayList(updatedEvents);
        Mockito.verify(strategy).notifyDataSetChanged(adapter, events, updatedEvents);
        Assert.assertEquals(updatedEvents.size(), adapter.getItemCount());
        onBindViewHolder(adapter, viewHolder, updatedEvents);
    }

    private void onBindViewHolder(ActivitiesStartEventsAdapter adapter, ActivityStartViewHolder viewHolder, List<ActivityStartEvent> events) {
        for (int i = 0; i < events.size(); i++) {
            adapter.onBindViewHolder(viewHolder, i);
            Mockito.verify(viewHolder).update(events.get(i));
        }
    }

}
