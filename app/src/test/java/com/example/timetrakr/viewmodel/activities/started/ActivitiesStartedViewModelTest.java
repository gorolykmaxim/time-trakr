package com.example.timetrakr.viewmodel.activities.started;

import com.example.timetrakr.TimeTrakrApplication;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.model.activity.events.ActivityStartEventFactory;
import com.example.timetrakr.model.activity.events.ActivityStartEventRepository;
import com.example.timetrakr.model.activity.events.AnotherActivityAlreadyStartedException;
import com.example.timetrakr.model.activity.events.NewActivityNameIsTooShortException;
import com.example.timetrakr.viewmodel.common.AsyncTaskExecutor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ActivitiesStartedViewModelTest {

    private String fishing;
    private LocalDateTime startDate;
    private ActivitiesStartedViewModel viewModel;
    private ActivityStartEventRepository repository;
    private ActivityStartEventFactory factory;
    private AsyncTaskExecutor executor;
    private LiveData<List<ActivityStartEvent>> activityStartEvents;

    @Before
    public void setUp() throws Exception {
        fishing = "Fishing";
        startDate = LocalDateTime.now();
        activityStartEvents = new MutableLiveData<>();
        repository = Mockito.mock(ActivityStartEventRepository.class);
        Mockito.when(repository.getObservableForAllForToday()).thenReturn(activityStartEvents);
        factory = Mockito.mock(ActivityStartEventFactory.class);
        executor = Mockito.mock(AsyncTaskExecutor.class);
        TimeTrakrApplication application = Mockito.mock(TimeTrakrApplication.class);
        Mockito.when(application.getActivityStartEventRepository()).thenReturn(repository);
        Mockito.when(application.getActivityStartEventFactory()).thenReturn(factory);
        Mockito.when(application.getExecutor()).thenReturn(executor);
        viewModel = new ActivitiesStartedViewModel(application);
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
    public void startNewActivity() throws NewActivityNameIsTooShortException, AnotherActivityAlreadyStartedException {
        ActivityStartEvent event = Mockito.mock(ActivityStartEvent.class);
        Mockito.when(factory.createNew(fishing, startDate)).thenReturn(event);
        viewModel.startNewActivity(fishing, startDate, null);
        ArgumentCaptor<Supplier> captor = ArgumentCaptor.forClass(Supplier.class);
        Mockito.verify(executor).execute(captor.capture(), Matchers.isNull(Consumer.class));
        Supplier supplier = captor.getValue();
        Object exception = supplier.get();
        Assert.assertNull(exception);
        Mockito.verify(repository).save(event);
    }

    @Test
    public void failedToStartNewActivityDueToShortName() throws NewActivityNameIsTooShortException {
        Mockito.when(factory.createNew(fishing, startDate)).thenThrow(Mockito.mock(NewActivityNameIsTooShortException.class));
        Consumer<Exception> listener = Mockito.mock(Consumer.class);
        viewModel.startNewActivity(fishing, startDate, listener);
        ArgumentCaptor<Supplier> captor = ArgumentCaptor.forClass(Supplier.class);
        Mockito.verify(executor).execute(captor.capture(), Matchers.eq(listener));
        Supplier supplier = captor.getValue();
        Assert.assertTrue(supplier.get() instanceof NewActivityNameIsTooShortException);
    }

    @Test
    public void failedToStartNewActivityAtThatDate() throws NewActivityNameIsTooShortException, AnotherActivityAlreadyStartedException {
        ActivityStartEvent event = Mockito.mock(ActivityStartEvent.class);
        Mockito.when(factory.createNew(fishing, startDate)).thenReturn(event);
        Mockito.doThrow(Mockito.mock(AnotherActivityAlreadyStartedException.class)).when(repository).save(event);
        Consumer<Exception> listener = Mockito.mock(Consumer.class);
        viewModel.startNewActivity(fishing, startDate, listener);
        ArgumentCaptor<Supplier> captor = ArgumentCaptor.forClass(Supplier.class);
        Mockito.verify(executor).execute(captor.capture(), Matchers.eq(listener));
        Supplier supplier = captor.getValue();
        Assert.assertTrue(supplier.get() instanceof AnotherActivityAlreadyStartedException);
    }

    @Test
    public void deleteActivityStart() {
        String activityStartDate = LocalDateTime.now().toString();
        ActivityStartEvent event = Mockito.mock(ActivityStartEvent.class);
        Mockito.when(factory.recreateFrom(fishing, activityStartDate)).thenReturn(event);
        viewModel.deleteActivityStart(fishing, activityStartDate);
        ArgumentCaptor<Supplier> captor = ArgumentCaptor.forClass(Supplier.class);
        Mockito.verify(executor).execute(captor.capture());
        Supplier supplier = captor.getValue();
        supplier.get();
        Mockito.verify(repository).delete(event);
    }
}
