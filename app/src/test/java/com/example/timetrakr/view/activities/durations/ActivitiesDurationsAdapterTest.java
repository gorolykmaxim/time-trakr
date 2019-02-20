package com.example.timetrakr.view.activities.durations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timetrakr.R;
import com.example.timetrakr.common.DurationFormatter;
import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.example.timetrakr.view.common.recycler.RecyclerViewAdapterStrategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LayoutInflater.class, RecyclerView.Adapter.class})
public class ActivitiesDurationsAdapterTest {

    private DurationFormatter formatter;
    private ActivitiesDurationsAdapter adapter;
    private RecyclerViewAdapterStrategy strategy;

    @Before
    public void setUp() throws Exception {
        strategy = Mockito.mock(RecyclerViewAdapterStrategy.class);
        formatter = new DurationFormatter();
        ActivityDurationViewHolder.ColorSet selectedColorSet = new ActivityDurationViewHolder.ColorSet(1, 1, 1);
        ActivityDurationViewHolder.ColorSet notSelectedColorSet = new ActivityDurationViewHolder.ColorSet(2, 2, 2);
        ActivityDurationViewHolderFactory factory = new ActivityDurationViewHolderFactory(formatter, selectedColorSet, notSelectedColorSet);
        adapter = new ActivitiesDurationsAdapter(factory);
        adapter.setStrategy(strategy);
    }

    @Test
    public void onCreateViewHolder() {
        adapter = PowerMockito.spy(adapter);
        ViewGroup viewGroup = Mockito.mock(ViewGroup.class);
        TextView activityNameTextView = Mockito.mock(TextView.class);
        Mockito.when(viewGroup.findViewById(R.id.activity_view_activity_text)).thenReturn(activityNameTextView);
        TextView activityDurationTextView = Mockito.mock(TextView.class);
        Mockito.when(viewGroup.findViewById(R.id.activity_view_duration_text)).thenReturn(activityDurationTextView);
        PowerMockito.mockStatic(LayoutInflater.class);
        LayoutInflater inflater = Mockito.mock(LayoutInflater.class);
        PowerMockito.when(LayoutInflater.from(viewGroup.getContext())).thenReturn(inflater);
        Mockito.when(inflater.inflate(R.layout.activity_view, viewGroup, false)).thenReturn(viewGroup);
        ArgumentCaptor<View.OnClickListener> itemSelectCaptor = ArgumentCaptor.forClass(View.OnClickListener.class);
        ActivitiesDurationsAdapter.OnItemSelectListener onItemSelectListener = Mockito.mock(ActivitiesDurationsAdapter.OnItemSelectListener.class);
        adapter.setOnItemSelectListener(onItemSelectListener);
        // Phew, mocking is done. This is android btw. Keep that in mind. It is really hard to test.
        ActivityDurationViewHolder viewHolder = adapter.onCreateViewHolder(viewGroup, 0);
        Mockito.verify(viewGroup).setOnClickListener(itemSelectCaptor.capture());
        View.OnClickListener itemSelect = itemSelectCaptor.getValue();
        ActivityDuration duration = new ActivityDuration("Hiking", Duration.ofMinutes(15));
        viewHolder.update(duration);
        Mockito.verify(activityNameTextView).setText(duration.getActivityName());
        Mockito.verify(activityDurationTextView).setText(formatter.format(duration.getDuration()));
        // Click on a not selected item.
        itemSelect.onClick(viewGroup);
        Mockito.verify(onItemSelectListener).onSelect(viewHolder, false);
        // Activity gets selected.
        PowerMockito.doNothing().when(adapter).notifyDataSetChanged();
        adapter.updateSelectedActivityDurations(Collections.singleton(duration.getActivityName()));
        Mockito.verify(adapter).notifyDataSetChanged();
        itemSelect.onClick(viewGroup);
        // Click on a selected item.
        Mockito.verify(onItemSelectListener).onSelect(viewHolder, true);
        // Select item.
        viewHolder.select();
        Mockito.verify(activityNameTextView).setTextColor(1);
        Mockito.verify(activityDurationTextView).setTextColor(1);
        Mockito.verify(viewGroup).setBackgroundColor(1);
        // De-select item.
        viewHolder.deselect();
        Mockito.verify(activityNameTextView).setTextColor(2);
        Mockito.verify(activityDurationTextView).setTextColor(2);
        Mockito.verify(viewGroup).setBackgroundColor(2);
    }

    @Test
    public void updateDataSet() {
        adapter = PowerMockito.spy(adapter);
        PowerMockito.doNothing().when(adapter).notifyDataSetChanged();
        // Initially we expected adapter to have no items.
        Assert.assertEquals(0, adapter.getItemCount());
        ActivityDurationViewHolder holder = Mockito.mock(ActivityDurationViewHolder.class);
        ActivityDuration activityDuration1 = new ActivityDuration("Biking", Duration.ofHours(1));
        ActivityDuration activityDuration2 = new ActivityDuration("Peeing", Duration.ofSeconds(35));
        ActivityDuration activityDuration3 = new ActivityDuration("Playing video-games", Duration.ofHours(3));
        adapter.updateActivityDurations(Arrays.asList(activityDuration1, activityDuration2, activityDuration3));
        // Duration adapter should not display activities that took less than 1 minutes, since
        // don't make to much sense to log them.
        List<ActivityDuration> expectedActivityDurations = Arrays.asList(activityDuration1, activityDuration3);
        Mockito.verify(strategy).notifyDataSetChanged(adapter, Collections.emptyList(), expectedActivityDurations);
        Assert.assertEquals(2, adapter.getItemCount());
        adapter.onBindViewHolder(holder, 0);
        Mockito.verify(holder).deselect();
        Mockito.verify(holder).update(activityDuration1);
        Mockito.reset(holder);
        adapter.onBindViewHolder(holder, 1);
        Mockito.verify(holder).update(activityDuration3);
        Mockito.verify(holder).deselect();
        Mockito.reset(holder);
        // Select biking activity.
        adapter.updateSelectedActivityDurations(Collections.singleton(activityDuration1.getActivityName()));
        // Now update adapter with new data set.
        adapter.updateActivityDurations(Arrays.asList(activityDuration1, activityDuration2));
        Mockito.verify(strategy).notifyDataSetChanged(adapter, expectedActivityDurations, Collections.singletonList(activityDuration1));
        Assert.assertEquals(1, adapter.getItemCount());
        adapter.onBindViewHolder(holder, 0);
        Mockito.verify(holder).update(activityDuration1);
        Mockito.verify(holder).select();
    }
}
