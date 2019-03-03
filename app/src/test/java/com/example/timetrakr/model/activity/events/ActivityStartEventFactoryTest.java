package com.example.timetrakr.model.activity.events;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

public class ActivityStartEventFactoryTest {

    private ActivityStartEventFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new ActivityStartEventFactory();
    }

    @Test
    public void createWithDefaultMinimalNameSize() throws NewActivityNameIsTooShortException, StartActivityInFutureException {
        String name = "BAL";
        LocalDateTime startDate = LocalDateTime.now();
        ActivityStartEvent event = factory.createNew(name, startDate);
        Assert.assertEquals(name, event.getActivityName());
        Assert.assertEquals(startDate, event.getStartDate());
    }

    @Test(expected = NewActivityNameIsTooShortException.class)
    public void failToCreateWithDefaultMinimalNameSize() throws NewActivityNameIsTooShortException, StartActivityInFutureException {
        factory.createNew("PA", LocalDateTime.now());
    }

    @Test
    public void createWithCustomMinimalNameSize() throws NewActivityNameIsTooShortException, StartActivityInFutureException {
        String name = "Horse riding";
        LocalDateTime startDate = LocalDateTime.now();
        factory.setMinimalActivityNameLength(4);
        ActivityStartEvent event = factory.createNew(name, startDate);
        Assert.assertEquals(name, event.getActivityName());
        Assert.assertEquals(startDate, event.getStartDate());
    }

    @Test
    public void failToCreateWithCustomMinimalNameSize() throws StartActivityInFutureException {
        try {
            factory.setMinimalActivityNameLength(5);
            factory.createNew("Work", LocalDateTime.now());
            Assert.fail("Creating activity with such a short name should throw an exception");
        } catch (NewActivityNameIsTooShortException e) {
            Assert.assertEquals("Work", e.getActivityName());
            Assert.assertEquals(5, e.getExpectedMinimalLength());
        }
    }

    @Test(expected = StartActivityInFutureException.class)
    public void failedToStartActivityInTheFuture() throws NewActivityNameIsTooShortException, StartActivityInFutureException {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        factory.createNew("Do the important stuff", tomorrow);
    }

    @Test
    public void recreateFromName() {
        // Using recreateFrom() should not validate name length
        String name = "WA";
        ActivityStartEvent event = factory.recreateFrom(name);
        Assert.assertEquals(name, event.getActivityName());
        Assert.assertTrue(event.getStartDate().compareTo(LocalDateTime.now()) <= 0);
    }

    @Test
    public void recreateFromNameAndDate() {
        // Using recreateFrom() should not validate name length
        String name = "CF";
        LocalDateTime dateTime = LocalDateTime.now();
        ActivityStartEvent event = factory.recreateFrom(name, dateTime.toString());
        Assert.assertEquals(name, event.getActivityName());
        Assert.assertEquals(dateTime, event.getStartDate());
    }

}
