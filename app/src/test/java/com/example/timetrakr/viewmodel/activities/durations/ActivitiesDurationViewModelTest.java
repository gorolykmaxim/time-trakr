package com.example.timetrakr.viewmodel.activities.durations;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.timetrakr.TimeTrakrApplication;
import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.example.timetrakr.model.activity.duration.ActivityDurationCalculator;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.model.activity.events.ActivityStartEventRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

public class ActivitiesDurationViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private TimeTrakrApplication application;
    private ActivityDurationCalculator durationCalculator;
    private ActivityStartEventRepository repository;
    private MutableLiveData<List<ActivityStartEvent>> repositoryObservable;
    private List<ActivityStartEvent> activityStartEvents;
    private List<ActivityDuration> expectedActivityDurations;
    private ActivitiesDurationViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        activityStartEvents = Collections.singletonList(Mockito.mock(ActivityStartEvent.class));
        expectedActivityDurations = Collections.singletonList(Mockito.mock(ActivityDuration.class));
        durationCalculator = Mockito.mock(ActivityDurationCalculator.class);
        Mockito.when(durationCalculator.calculateDurationsFromEvents(activityStartEvents)).thenReturn(expectedActivityDurations);
        repository = Mockito.mock(ActivityStartEventRepository.class);
        repositoryObservable = new MutableLiveData<>();
        Mockito.when(repository.getObservableForAllForToday()).thenReturn(repositoryObservable);
        application = Mockito.mock(TimeTrakrApplication.class);
        Mockito.when(application.getActivityDurationCalculator()).thenReturn(durationCalculator);
        Mockito.when(application.getActivityStartEventRepository()).thenReturn(repository);
        viewModel = new ActivitiesDurationViewModel(application);
    }

    @Test
    public void receiveActivitiesFromRepositoryObservable() {
        LiveData<List<ActivityDuration>> activityDurations = viewModel.getActivityDurations();
        // Need to activate LiveData or else it won't receive any notifications from a LiveData
        // it listens to.
        activityDurations.observeForever(list -> {});
        repositoryObservable.setValue(activityStartEvents);
        Assert.assertEquals(expectedActivityDurations, activityDurations.getValue());
    }

    @Test
    public void inCaseOfNullDurationsShouldNotChange() {
        LiveData<List<ActivityDuration>> activityDurations = viewModel.getActivityDurations();
        repositoryObservable.setValue(activityStartEvents);
        activityDurations.observeForever(list -> {});
        Assert.assertEquals(expectedActivityDurations, activityDurations.getValue());
        repositoryObservable.setValue(null);
        Assert.assertEquals(expectedActivityDurations, activityDurations.getValue());
    }

    @Test
    public void recalculateActivityDurations() {
        Mockito.when(durationCalculator.calculateDurationsFromEvents(activityStartEvents)).thenReturn(Collections.emptyList());
        LiveData<List<ActivityDuration>> activityDurations = viewModel.getActivityDurations();
        repositoryObservable.setValue(activityStartEvents);
        activityDurations.observeForever(list -> {});
        Assert.assertEquals(Collections.emptyList(), activityDurations.getValue());
        // Now duration calculator will behave differently receiving the same set of activity start events.
        Mockito.when(durationCalculator.calculateDurationsFromEvents(activityStartEvents)).thenReturn(expectedActivityDurations);
        viewModel.recalculateActivityDurations();
        Assert.assertEquals(expectedActivityDurations, activityDurations.getValue());
    }
}
