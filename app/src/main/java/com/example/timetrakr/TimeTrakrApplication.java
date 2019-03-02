package com.example.timetrakr;

import android.app.Application;

import com.example.timetrakr.common.DurationFormatter;
import com.example.timetrakr.model.activity.duration.ActivityDuration;
import com.example.timetrakr.model.activity.duration.ActivityDurationCalculator;
import com.example.timetrakr.model.activity.duration.ActivityDurationFactory;
import com.example.timetrakr.model.activity.events.ActivityStartEventFactory;
import com.example.timetrakr.model.activity.events.ActivityStartEventRepository;
import com.example.timetrakr.model.messages.Message;
import com.example.timetrakr.model.messages.MessageRepository;
import com.example.timetrakr.model.messages.common.And;
import com.example.timetrakr.model.messages.common.CountIs;
import com.example.timetrakr.model.messages.common.CountIsGreaterThan;
import com.example.timetrakr.model.messages.common.Not;
import com.example.timetrakr.model.messages.durations.ActivityDurationWithDurationShorterThanBuilder;
import com.example.timetrakr.model.messages.durations.ActivityNameWithDurationShorterThanBuilder;
import com.example.timetrakr.model.messages.durations.HasDurationLongerThan;
import com.example.timetrakr.model.messages.durations.HasDurationShorterThan;
import com.example.timetrakr.persistence.TimeTrakrDatabase;

import java.time.Duration;
import java.util.List;
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
    private MessageRepository<List<ActivityDuration>> durationMessagesRepository;

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
        // Initialize repository of activity durations related messages.
        durationMessagesRepository = new MessageRepository<>();
        durationMessagesRepository.save(new CountIs<>(0),
                new Message<>(getString(R.string.duration_message_1)),
                new Message<>(getString(R.string.duration_message_2)),
                new Message<>(getString(R.string.duration_message_3)));
        durationMessagesRepository.save(new CountIsGreaterThan<>(0),
                new Message<>(getString(R.string.duration_message_4)),
                new Message<>(getString(R.string.duration_message_5)),
                new Message<>(getString(R.string.duration_message_6)),
                new Message<>(getString(R.string.duration_message_7)),
                new Message<>(getString(R.string.duration_message_8)),
                new Message<>(getString(R.string.duration_message_9)));
        durationMessagesRepository.save(new And<>(new CountIsGreaterThan<>(0), new Not<>(new HasDurationLongerThan(Duration.ofMinutes(1)))),
                new Message<>(getString(R.string.duration_message_10)),
                new Message<>(getString(R.string.duration_message_11)));
        Duration duration = Duration.ofMinutes(30);
        DurationFormatter formatter = new DurationFormatter();
        durationMessagesRepository.save(new HasDurationShorterThan(duration),
                new Message<>(getString(R.string.duration_message_12), new ActivityNameWithDurationShorterThanBuilder(duration)),
                new Message<>(getString(R.string.duration_message_13), new ActivityNameWithDurationShorterThanBuilder(duration)),
                new Message<>(getString(R.string.duration_message_14), new ActivityNameWithDurationShorterThanBuilder(duration), new ActivityDurationWithDurationShorterThanBuilder(duration, formatter)));
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

    /**
     * Return repository of messages to display in the activity durations view.
     *
     * @return repository of activity durations related messages
     */
    public MessageRepository<List<ActivityDuration>> getDurationMessagesRepository() {
        return durationMessagesRepository;
    }
}
