package com.example.timetrakr.model.activity.duration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ActivityDurationSelectionTest {
    
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private ActivityDurationSelection selection;
    private MutableLiveData<List<ActivityDuration>> observableActivityDurations;

    @Before
    public void setUp() throws Exception {
        observableActivityDurations = new MutableLiveData<>();
        selection = new ActivityDurationSelection(observableActivityDurations);
    }

    @Test
    public void recalculateTotalDurationBasedOnCurrentSelection() {
        // Need to activate LiveData or else it won't receive any notifications from a LiveData
        // it listens to.
        selection.getObservableTotalDuration().observeForever(list -> {});
        String playingVideoGames = "Playing video games";
        String goingOutside = "Going outside";
        // First lets create some initial state with two activities selected.
        selection.add(new ActivityDuration(playingVideoGames, Duration.ofHours(4)));
        selection.add(new ActivityDuration(goingOutside, Duration.ofHours(1)));
        Duration totalDuration = selection.getObservableTotalDuration().getValue();
        Assert.assertEquals(Duration.ofHours(5), totalDuration);
        // Now lets update durations list.
        List<ActivityDuration> updatedActivityDurations = new ArrayList<>();
        updatedActivityDurations.add(new ActivityDuration(playingVideoGames, Duration.ofHours(5)));
        updatedActivityDurations.add(new ActivityDuration(goingOutside, Duration.ofHours(3)));
        observableActivityDurations.setValue(updatedActivityDurations);
        totalDuration = selection.getObservableTotalDuration().getValue();
        Assert.assertEquals(Duration.ofHours(8), totalDuration);
        selection.clear();
        // In case there are no items selected, updating durations of activities should not change
        // the fact that the total duration of selected activities (which are none) should be zero.
        observableActivityDurations.setValue(updatedActivityDurations);
        totalDuration = selection.getObservableTotalDuration().getValue();
        Assert.assertEquals(Duration.ZERO, totalDuration);
    }

    @Test
    public void calculateTotalDuration() {
        ActivityDuration working = new ActivityDuration("Working", Duration.ofHours(1));
        ActivityDuration coffeeBreak = new ActivityDuration("Coffee break", Duration.ofMinutes(15));
        LiveData<Duration> totalDuration = selection.getObservableTotalDuration();
        LiveData<Set<String>> selectedActivities = selection.getObservableSelectedActivities();
        // User selects "Working".
        selection.add(working);
        Assert.assertEquals(working.getDuration(), totalDuration.getValue());
        Assert.assertTrue(selectedActivities.getValue().contains(working.getActivityName()));
        // User selects "Working" again by mistake. Nothing should change though.
        selection.add(working);
        Assert.assertEquals(working.getDuration(), totalDuration.getValue());
        Assert.assertEquals(1, selectedActivities.getValue().size());
        // User selects "Coffee break".
        selection.add(coffeeBreak);
        Assert.assertEquals(working.getDuration().plus(coffeeBreak.getDuration()), totalDuration.getValue());
        Assert.assertTrue(selectedActivities.getValue().containsAll(Arrays.asList(working.getActivityName(), coffeeBreak.getActivityName())));
        // User de-selects "Coffee break".
        selection.remove(coffeeBreak);
        Assert.assertEquals(working.getDuration(), totalDuration.getValue());
        Assert.assertTrue(selectedActivities.getValue().contains(working.getActivityName()));
        Assert.assertEquals(1, selectedActivities.getValue().size());
        // User de-selects "Coffee break" again by mistake. Nothing should change though.
        selection.remove(coffeeBreak);
        Assert.assertEquals(working.getDuration(), totalDuration.getValue());
        Assert.assertTrue(selectedActivities.getValue().contains(working.getActivityName()));
        Assert.assertEquals(1, selectedActivities.getValue().size());
        // User selects "Coffee break" again and clear selection completely.
        selection.add(coffeeBreak);
        selection.clear();
        Assert.assertEquals(Duration.ZERO, totalDuration.getValue());
        Assert.assertTrue(!selectedActivities.getValue().contains(working.getActivityName()));
        Assert.assertTrue(!selectedActivities.getValue().contains(coffeeBreak.getActivityName()));
        Assert.assertEquals(0, selectedActivities.getValue().size());
        // User selects only "Coffee break".
        selection.add(coffeeBreak);
        Assert.assertEquals(coffeeBreak.getDuration(), totalDuration.getValue());
        Assert.assertTrue(selectedActivities.getValue().contains(coffeeBreak.getActivityName()));
        Assert.assertEquals(1, selectedActivities.getValue().size());
        // User de-selects "Coffee break".
        selection.remove(coffeeBreak);
        Assert.assertEquals(Duration.ZERO, totalDuration.getValue());
        Assert.assertTrue(!selectedActivities.getValue().contains(coffeeBreak.getActivityName()));
        Assert.assertEquals(0, selectedActivities.getValue().size());
        // User de-selects "Working" by mistake. Nothing should change though.
        selection.remove(working);
        Assert.assertEquals(Duration.ZERO, totalDuration.getValue());
        Assert.assertEquals(0, selectedActivities.getValue().size());
    }

}
