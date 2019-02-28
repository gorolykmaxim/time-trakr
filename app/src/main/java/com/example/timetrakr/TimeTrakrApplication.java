package com.example.timetrakr;

import android.app.Application;

import com.example.timetrakr.model.activity.duration.ActivityDurationCalculator;
import com.example.timetrakr.model.activity.duration.ActivityDurationFactory;
import com.example.timetrakr.model.activity.events.ActivityStartEventFactory;
import com.example.timetrakr.model.activity.events.ActivityStartEventRepository;
import com.example.timetrakr.persistence.TimeTrakrDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Room;

/**
 * Service registry of the time-trakr application.
 */
public class TimeTrakrApplication extends Application {

    private ActivityStartEventRepository activityStartEventRepository;
    private ActivityStartEventFactory activityStartEventFactory;
    private ActivityDurationFactory activityDurationFactory;
    private ActivityDurationCalculator activityDurationCalculator;
    private ExecutorService executorService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate() {
        super.onCreate();
        TimeTrakrDatabase database = Room
                .databaseBuilder(this, TimeTrakrDatabase.class, TimeTrakrDatabase.NAME)
                .fallbackToDestructiveMigration()
                .build();
        activityStartEventRepository = new ActivityStartEventRepository(database.getActivityStartEventDao());
        activityStartEventFactory = new ActivityStartEventFactory();
        activityDurationFactory = new ActivityDurationFactory();
        activityDurationCalculator = new ActivityDurationCalculator(activityDurationFactory, activityStartEventFactory);
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Return activity start events repository instance.
     *
     * @return activity start events repository
     */
    public ActivityStartEventRepository getActivityStartEventRepository() {
        return activityStartEventRepository;
    }

    /**
     * Return activity start events factory instance.
     *
     * @return activity start events factory
     */
    public ActivityStartEventFactory getActivityStartEventFactory() {
        return activityStartEventFactory;
    }

    /**
     * Return activity duration factory instance.
     *
     * @return activity duration factory
     */
    public ActivityDurationFactory getActivityDurationFactory() {
        return activityDurationFactory;
    }

    /**
     * Return activity duration calculator instance.
     *
     * @return activity duration calculator
     */
    public ActivityDurationCalculator getActivityDurationCalculator() {
        return activityDurationCalculator;
    }

    /**
     * Return executor service of the application.
     *
     * @return executor service
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }
}
