package com.example.timetrakr.viewmodel.activities.durations;

import com.example.timetrakr.TimeTrakrApplication;
import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.example.timetrakr.model.activity.duration.ActivityDurationCalculator;
import com.example.timetrakr.model.activity.duration.ActivityDurationSelection;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.model.activity.events.ActivityStartEventRepository;
import com.example.timetrakr.model.messages.MessageRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ActivitiesDurationViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private TimeTrakrApplication application;
    private ActivityDurationCalculator durationCalculator;
    private ActivityStartEventRepository repository;
    private MutableLiveData<List<ActivityStartEvent>> repositoryObservable;
    private List<ActivityStartEvent> activityStartEvents;
    private List<ActivityDuration> expectedActivityDurations;
    private ActivityDuration activityDuration;
    private ActivitiesDurationViewModel viewModel;
    private ActivityDurationSelection activityDurationSelection;
    private MessageRepository<List<ActivityDuration>> messageRepository;
    private String message;

    @Before
    public void setUp() throws Exception {
        activityStartEvents = Collections.singletonList(Mockito.mock(ActivityStartEvent.class));
        activityDuration = Mockito.mock(ActivityDuration.class);
        expectedActivityDurations = Collections.singletonList(activityDuration);
        durationCalculator = Mockito.mock(ActivityDurationCalculator.class);
        message = "Hello.";
        messageRepository = Mockito.mock(MessageRepository.class);
        Mockito.when(messageRepository.findOneThatAppliesTo(expectedActivityDurations)).thenReturn(message);
        Mockito.when(durationCalculator.calculateDurationsFromEvents(activityStartEvents)).thenReturn(expectedActivityDurations);
        repository = Mockito.mock(ActivityStartEventRepository.class);
        repositoryObservable = new MutableLiveData<>();
        Mockito.when(repository.getObservableForAllForToday()).thenReturn(repositoryObservable);
        application = Mockito.mock(TimeTrakrApplication.class);
        Mockito.when(application.getActivityDurationCalculator()).thenReturn(durationCalculator);
        Mockito.when(application.getActivityStartEventRepository()).thenReturn(repository);
        Mockito.when(application.getDurationMessagesRepository()).thenReturn(messageRepository);
        viewModel = new ActivitiesDurationViewModel(application);
        activityDurationSelection = Mockito.mock(ActivityDurationSelection.class);
        viewModel.setActivityDurationSelection(activityDurationSelection);
    }

    @Test
    public void receiveActivitiesFromRepositoryObservable() {
        LiveData<List<ActivityDuration>> activityDurations = viewModel.getActivityDurations();
        LiveData<String> observableMessage = viewModel.getObservableMessage();
        // Need to activate LiveData or else it won't receive any notifications from a LiveData
        // it listens to.
        activityDurations.observeForever(list -> {});
        observableMessage.observeForever(message -> {});
        repositoryObservable.setValue(activityStartEvents);
        Assert.assertEquals(expectedActivityDurations, activityDurations.getValue());
        Assert.assertEquals(message, observableMessage.getValue());
    }

    @Test
    public void inCaseOfNullDurationsShouldNotChange() {
        LiveData<List<ActivityDuration>> activityDurations = viewModel.getActivityDurations();
        LiveData<String> observableMessage = viewModel.getObservableMessage();
        repositoryObservable.setValue(activityStartEvents);
        activityDurations.observeForever(list -> {});
        observableMessage.observeForever(message -> {});
        Assert.assertEquals(expectedActivityDurations, activityDurations.getValue());
        Assert.assertEquals(message, observableMessage.getValue());
        repositoryObservable.setValue(null);
        Assert.assertEquals(expectedActivityDurations, activityDurations.getValue());
        Mockito.verify(messageRepository, Mockito.never()).findOneThatAppliesTo(null);
        Assert.assertEquals(message, observableMessage.getValue());
    }

    @Test
    public void recalculateActivityDurations() {
        String emptyListMessage = "The list is empty";
        Mockito.when(durationCalculator.calculateDurationsFromEvents(activityStartEvents)).thenReturn(Collections.emptyList());
        Mockito.when(messageRepository.findOneThatAppliesTo(Collections.emptyList())).thenReturn(emptyListMessage);
        LiveData<List<ActivityDuration>> activityDurations = viewModel.getActivityDurations();
        LiveData<String> observableMessage = viewModel.getObservableMessage();
        repositoryObservable.setValue(activityStartEvents);
        activityDurations.observeForever(list -> {});
        observableMessage.observeForever(message -> {});
        Assert.assertEquals(Collections.emptyList(), activityDurations.getValue());
        Assert.assertEquals(emptyListMessage, observableMessage.getValue());
        // Now duration calculator will behave differently receiving the same set of activity start events.
        Mockito.when(durationCalculator.calculateDurationsFromEvents(activityStartEvents)).thenReturn(expectedActivityDurations);
        viewModel.recalculateActivityDurations();
        Assert.assertEquals(expectedActivityDurations, activityDurations.getValue());
        Assert.assertEquals(message, observableMessage.getValue());
    }

    @Test
    public void selectActivityDuration() {
        viewModel.selectActivityDuration(activityDuration);
        Mockito.verify(activityDurationSelection).add(activityDuration);
    }

    @Test
    public void deselectActivityDuration() {
        viewModel.deselectActivityDuration(activityDuration);
        Mockito.verify(activityDurationSelection).remove(activityDuration);
    }

    @Test
    public void clearSelection() {
        viewModel.clearSelectedActivityDurations();
        Mockito.verify(activityDurationSelection).clear();
    }

    @Test
    public void getObservableTotalDuration() {
        LiveData<Duration> expectedObservable = Mockito.mock(LiveData.class);
        Mockito.when(activityDurationSelection.getObservableTotalDuration()).thenReturn(expectedObservable);
        LiveData<Duration> observable = viewModel.getObservableTotalDuration();
        Assert.assertEquals(expectedObservable, observable);
    }

    @Test
    public void getObservableSelectedActivities() {
        LiveData<Set<String>> expectedObservable = Mockito.mock(LiveData.class);
        Mockito.when(activityDurationSelection.getObservableSelectedActivities()).thenReturn(expectedObservable);
        LiveData<Set<String>> observable = viewModel.getObservableSelectedActivities();
        Assert.assertEquals(expectedObservable, observable);
    }

}
