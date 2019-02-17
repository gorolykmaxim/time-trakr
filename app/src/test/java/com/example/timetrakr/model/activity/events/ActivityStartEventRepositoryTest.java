package com.example.timetrakr.model.activity.events;

import androidx.lifecycle.LiveData;

import com.example.timetrakr.persistence.ActivityStartEventDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ActivityStartEventRepositoryTest {

    private ActivityStartEventDao dao;
    private ActivityStartEventRepository repository;

    @Before
    public void setUp() throws Exception {
        dao = Mockito.mock(ActivityStartEventDao.class);
        repository = new ActivityStartEventRepository(dao);
    }

    @Test
    public void getObservableForAllForToday() {
        LiveData<List<ActivityStartEvent>> expectedObservable = Mockito.mock(LiveData.class);
        Mockito.when(dao.getObservableForAllStartedBefore(LocalDate.now().atStartOfDay())).thenReturn(expectedObservable);
        LiveData<List<ActivityStartEvent>> observable = repository.getObservableForAllForToday();
        Assert.assertEquals(expectedObservable, observable);
    }

    @Test
    public void findAllForTodayWithNameLike() {
        List<ActivityStartEvent> expectedEvents = Mockito.mock(List.class);
        Mockito.when(dao.getAllStartedBeforeWithNameLike(LocalDate.now().atStartOfDay(), "%hobby%")).thenReturn(expectedEvents);
        List<ActivityStartEvent> events = repository.findAllForTodayWithNameLike("hobby");
        Assert.assertEquals(expectedEvents, events);
    }

    @Test
    public void save() {
        ActivityStartEvent event = new ActivityStartEvent("Cleaning", LocalDateTime.now());
        repository.save(event);
        Mockito.verify(dao).insert(event);
    }

    @Test
    public void delete() {
        ActivityStartEvent event = new ActivityStartEvent("Work", LocalDateTime.now());
        repository.delete(event);
        Mockito.verify(dao).delete(event);
    }

}
