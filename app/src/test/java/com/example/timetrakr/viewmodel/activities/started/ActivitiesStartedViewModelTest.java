package com.example.timetrakr.viewmodel.activities.started;

import com.example.timetrakr.TimeTrakrApplication;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.model.activity.events.ActivityStartEventFactory;
import com.example.timetrakr.model.activity.events.ActivityStartEventRepository;
import com.example.timetrakr.model.activity.events.AnotherActivityAlreadyStartedException;
import com.example.timetrakr.model.activity.events.NewActivityNameIsTooShortException;
import com.example.timetrakr.model.messages.MessageRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class ActivitiesStartedViewModelTest {

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private String fishing;
    private LocalDateTime startDate;
    private ActivitiesStartedViewModel viewModel;
    private ActivityStartEventRepository repository;
    private ActivityStartEventFactory factory;
    private ExecutorService executor;
    private MessageRepository<LocalDateTime> messageRepository;
    private MutableLiveData<List<ActivityStartEvent>> activityStartEvents;
    private Observer<NewActivityNameIsTooShortException> nameIsTooShortObserver;
    private Observer<AnotherActivityAlreadyStartedException> activityAlreadyStartedObserver;

    @Before
    public void setUp() throws Exception {
        fishing = "Fishing";
        startDate = LocalDateTime.now();
        activityStartEvents = new MutableLiveData<>();
        repository = Mockito.mock(ActivityStartEventRepository.class);
        Mockito.when(repository.getObservableForAllForToday()).thenReturn(activityStartEvents);
        factory = Mockito.mock(ActivityStartEventFactory.class);
        executor = Mockito.mock(ExecutorService.class);
        messageRepository = Mockito.mock(MessageRepository.class);
        nameIsTooShortObserver = Mockito.mock(Observer.class);
        activityAlreadyStartedObserver = Mockito.mock(Observer.class);
        TimeTrakrApplication application = Mockito.mock(TimeTrakrApplication.class);
        Mockito.when(application.getActivityStartEventRepository()).thenReturn(repository);
        Mockito.when(application.getActivityStartEventFactory()).thenReturn(factory);
        Mockito.when(application.getExecutorService()).thenReturn(executor);
        Mockito.when(application.getActivityMessagesRepository()).thenReturn(messageRepository);
        viewModel = new ActivitiesStartedViewModel(application);
        viewModel.getNameIsTooShortObservable().observeForever(nameIsTooShortObserver);
        viewModel.getActivityAlreadyStartedObservable().observeForever(activityAlreadyStartedObserver);
    }

    @Test
    public void getActivityStartEvents() {
        Assert.assertEquals(activityStartEvents, viewModel.getActivityStartEvents());
    }

    @Test
    public void getActivitiesWithSimilarNames() {
        List<ActivityStartEvent> expectedStartEvents = Collections.singletonList(Mockito.mock(ActivityStartEvent.class));
        Mockito.when(repository.findAllForTodayWithNameLike(fishing)).thenReturn(expectedStartEvents);
        List<ActivityStartEvent> startEvents = viewModel.getActivitiesWithSimilarNames(fishing);
        Assert.assertEquals(expectedStartEvents, startEvents);
    }

    @Test
    public void getObservableMessage() {
        LiveData<String> observableMessage = viewModel.getObservableMessage();
        observableMessage.observeForever(message -> {});
        // Since list of activities is empty, we expect to see a friendly message.
        String expectedMessage = "Hi :)";
        Mockito.when(messageRepository.findOneThatAppliesTo(Mockito.any(LocalDateTime.class))).thenReturn(expectedMessage);
        activityStartEvents.setValue(Collections.emptyList());
        Assert.assertEquals(expectedMessage, observableMessage.getValue());
        // Now when list is not empty anymore, there should be no message updates.
        Mockito.when(messageRepository.findOneThatAppliesTo(Mockito.any(LocalDateTime.class))).thenReturn("Get off my face!!!");
        activityStartEvents.setValue(Collections.singletonList(Mockito.mock(ActivityStartEvent.class)));
        Assert.assertEquals(expectedMessage, observableMessage.getValue());
        // Even if list is null, we should not update the message.
        activityStartEvents.setValue(null);
        Assert.assertEquals(expectedMessage, observableMessage.getValue());
    }

    @Test
    public void startNewActivity() throws NewActivityNameIsTooShortException, AnotherActivityAlreadyStartedException {
        ActivityStartEvent event = Mockito.mock(ActivityStartEvent.class);
        Mockito.when(factory.createNew(fishing, startDate)).thenReturn(event);
        viewModel.startNewActivity(fishing, startDate);
        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        Mockito.verify(executor).execute(captor.capture());
        Runnable runnable = captor.getValue();
        runnable.run();
        Mockito.verify(repository).save(event);
        Mockito.verify(nameIsTooShortObserver, Mockito.never()).onChanged(Mockito.any());
        Mockito.verify(activityAlreadyStartedObserver, Mockito.never()).onChanged(Mockito.any());
    }

    @Test
    public void failedToStartNewActivityDueToShortName() throws NewActivityNameIsTooShortException {
        Mockito.when(factory.createNew(fishing, startDate)).thenThrow(Mockito.mock(NewActivityNameIsTooShortException.class));
        viewModel.startNewActivity(fishing, startDate);
        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        Mockito.verify(executor).execute(captor.capture());
        Runnable runnable = captor.getValue();
        runnable.run();
        Mockito.verify(nameIsTooShortObserver).onChanged(Mockito.any(NewActivityNameIsTooShortException.class));
    }

    @Test
    public void failedToStartNewActivityAtThatDate() throws NewActivityNameIsTooShortException, AnotherActivityAlreadyStartedException {
        ActivityStartEvent event = Mockito.mock(ActivityStartEvent.class);
        Mockito.when(factory.createNew(fishing, startDate)).thenReturn(event);
        Mockito.doThrow(Mockito.mock(AnotherActivityAlreadyStartedException.class)).when(repository).save(event);
        viewModel.startNewActivity(fishing, startDate);
        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        Mockito.verify(executor).execute(captor.capture());
        Runnable runnable = captor.getValue();
        runnable.run();
        Mockito.verify(activityAlreadyStartedObserver).onChanged(Mockito.any(AnotherActivityAlreadyStartedException.class));
    }

    @Test
    public void deleteActivityStart() {
        String activityStartDate = LocalDateTime.now().toString();
        ActivityStartEvent event = Mockito.mock(ActivityStartEvent.class);
        Mockito.when(factory.recreateFrom(fishing, activityStartDate)).thenReturn(event);
        viewModel.deleteActivityStart(fishing, activityStartDate);
        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        Mockito.verify(executor).execute(captor.capture());
        Runnable runnable = captor.getValue();
        runnable.run();
        Mockito.verify(repository).delete(event);
    }
}
