package com.example.timetrakr.view.activities.durations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timetrakr.R;
import com.example.timetrakr.view.common.ClosableTextView;
import com.example.timetrakr.viewmodel.activities.durations.ActivitiesDurationViewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ViewModelProviders.class})
public class ActivitiesDurationsFragmentTest {

    private ActivitiesDurationViewModel viewModel;
    private ViewModelProvider provider;

    @Before
    public void setUp() throws Exception {
        viewModel = Mockito.mock(ActivitiesDurationViewModel.class);
        Mockito.when(viewModel.getActivityDurations()).thenReturn(Mockito.mock(LiveData.class));
        Mockito.when(viewModel.getObservableSelectedActivities()).thenReturn(Mockito.mock(LiveData.class));
        Mockito.when(viewModel.getObservableTotalDuration()).thenReturn(Mockito.mock(LiveData.class));
        provider = Mockito.mock(ViewModelProvider.class);
        Mockito.when(provider.get(ActivitiesDurationViewModel.class)).thenReturn(viewModel);
    }

    @Test
    public void test() {
        Fragment fragment = new ActivitiesDurationsFragment();
        PowerMockito.mockStatic(ViewModelProviders.class);
        PowerMockito.when(ViewModelProviders.of(fragment)).thenReturn(provider);
        LayoutInflater inflater = Mockito.mock(LayoutInflater.class);
        ViewGroup container = Mockito.mock(ViewGroup.class);
        ViewGroup root = Mockito.mock(ViewGroup.class);
        Context context = Mockito.mock(Context.class);
        Mockito.when(context.getColor(Mockito.anyInt())).thenReturn(0);
        Mockito.when(context.getString(Mockito.anyInt())).thenReturn("Any string");
        Mockito.when(root.getContext()).thenReturn(context);
        RecyclerView recyclerView = Mockito.mock(RecyclerView.class);
        Mockito.when(root.findViewById(R.id.activities_durations_recycler_view)).thenReturn(recyclerView);
        ClosableTextView totalDurationView = Mockito.mock(ClosableTextView.class);
        Mockito.when(root.findViewById(R.id.total_duration)).thenReturn(totalDurationView);
        Mockito.when(inflater.inflate(R.layout.activities_durations_view, container, false)).thenReturn(root);
        // onCreateView() should properly initialize everything.
        View view = fragment.onCreateView(inflater, container, null);
        Assert.assertEquals(root, view);
        Mockito.verify(recyclerView).setLayoutManager(Mockito.notNull(RecyclerView.LayoutManager.class));
        Mockito.verify(recyclerView).setAdapter(Mockito.notNull(RecyclerView.Adapter.class));
        Mockito.verify(totalDurationView).setOnCloseListener(Mockito.notNull(ClosableTextView.OnCloseListener.class));
        Mockito.verify(viewModel.getActivityDurations()).observe(Mockito.eq(fragment), Mockito.notNull(Observer.class));
        Mockito.verify(viewModel.getObservableSelectedActivities()).observe(Mockito.eq(fragment), Mockito.notNull(Observer.class));
        Mockito.verify(viewModel.getObservableTotalDuration()).observe(Mockito.eq(fragment), Mockito.notNull(Observer.class));
        // Each time fragment is re-drawn, it's view model should recalculate activity durations,
        // so it always fresh values.
        fragment.onStart();
        Mockito.verify(viewModel).recalculateActivityDurations();
    }
}
