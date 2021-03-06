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
import com.example.timetrakr.model.messages.common.Any;
import com.example.timetrakr.model.messages.common.CountIs;
import com.example.timetrakr.model.messages.common.CountIsGreaterThan;
import com.example.timetrakr.model.messages.common.Not;
import com.example.timetrakr.model.messages.common.Or;
import com.example.timetrakr.model.messages.dates.DayOfTheWeekBuilder;
import com.example.timetrakr.model.messages.dates.IsTimeEarlierThan;
import com.example.timetrakr.model.messages.dates.IsTimeLaterThan;
import com.example.timetrakr.model.messages.dates.IsWeekendDay;
import com.example.timetrakr.model.messages.dates.TimeOfTheDayBuilder;
import com.example.timetrakr.model.messages.durations.ActivityDurationWithDurationShorterThanBuilder;
import com.example.timetrakr.model.messages.durations.ActivityNameWithDurationLongerThanBuilder;
import com.example.timetrakr.model.messages.durations.ActivityNameWithDurationShorterThanBuilder;
import com.example.timetrakr.model.messages.durations.HasDurationLongerThan;
import com.example.timetrakr.model.messages.durations.HasDurationShorterThan;
import com.example.timetrakr.model.messages.durations.TotalDurationIsLongerThan;
import com.example.timetrakr.persistence.TimeTrakrDatabase;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private MessageRepository<LocalDateTime> activityMessagesRepository;

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
        Duration duration = Duration.ofMinutes(30);
        DurationFormatter durationFormatter = new DurationFormatter();
        durationMessagesRepository.save(new HasDurationShorterThan(duration),
                new Message<>(getString(R.string.duration_message_12), new ActivityNameWithDurationShorterThanBuilder(duration)),
                new Message<>(getString(R.string.duration_message_13), new ActivityNameWithDurationShorterThanBuilder(duration)),
                new Message<>(getString(R.string.duration_message_14), new ActivityNameWithDurationShorterThanBuilder(duration), new ActivityDurationWithDurationShorterThanBuilder(duration, durationFormatter)));
        durationMessagesRepository.save(new And<>(new CountIsGreaterThan<>(0), new Not<>(new HasDurationLongerThan(Duration.ofMinutes(1)))),
                new Message<>(getString(R.string.duration_message_10)),
                new Message<>(getString(R.string.duration_message_11)));
        duration = Duration.ofHours(4);
        durationMessagesRepository.save(new And<>(new CountIs<>(1), new HasDurationLongerThan(duration)),
                new Message<>(getString(R.string.duration_message_15), new ActivityNameWithDurationLongerThanBuilder(duration)),
                new Message<>(getString(R.string.duration_message_16), new ActivityNameWithDurationLongerThanBuilder(duration)),
                new Message<>(getString(R.string.duration_message_17), new ActivityNameWithDurationLongerThanBuilder(duration)));
        durationMessagesRepository.save(new CountIsGreaterThan<>(4), new Message<>(getString(R.string.duration_message_18)));
        durationMessagesRepository.save(new TotalDurationIsLongerThan(Duration.ofHours(8)),
                new Message<>(getString(R.string.duration_message_19)),
                new Message<>(getString(R.string.duration_message_20)),
                new Message<>(getString(R.string.duration_message_21)));
        durationMessagesRepository.save(new TotalDurationIsLongerThan(Duration.ofHours(9)),
                new Message<>(getString(R.string.duration_message_22)),
                new Message<>(getString(R.string.duration_message_23)),
                new Message<>(getString(R.string.duration_message_24)));
        // Initialize repository of activity start events related messages.
        activityMessagesRepository = new MessageRepository<>();
        activityMessagesRepository.save(new Any<>(),
                new Message<>(getString(R.string.activity_start_message1)),
                new Message<>(getString(R.string.activity_start_message2)));
        activityMessagesRepository.save(new IsWeekendDay(),
                new Message<>(getString(R.string.activity_start_message3), new DayOfTheWeekBuilder()),
                new Message<>(getString(R.string.activity_start_message4), new DayOfTheWeekBuilder()),
                new Message<>(getString(R.string.activity_start_message5), new DayOfTheWeekBuilder()),
                new Message<>(getString(R.string.activity_start_message6), new DayOfTheWeekBuilder()));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("H:mm");
        activityMessagesRepository.save(new Or<>(new IsTimeLaterThan(LocalTime.of(20, 0)), new IsTimeEarlierThan(LocalTime.of(8, 0))),
                new Message<>(getString(R.string.activity_start_message7), new TimeOfTheDayBuilder(dateTimeFormatter)));
        activityMessagesRepository.save(new IsTimeEarlierThan(LocalTime.of(7, 0)),
                new Message<>(getString(R.string.activity_start_message8)));
        activityMessagesRepository.save(new And<>(new Not<>(new IsWeekendDay()), new IsTimeLaterThan(LocalTime.of(10, 0))),
                new Message<>(getString(R.string.activity_start_message9)));
        activityMessagesRepository.save(new And<>(new IsWeekendDay(), new Or<>(new IsTimeLaterThan(LocalTime.of(18, 0)), new IsTimeEarlierThan(LocalTime.of(12, 0)))),
                new Message<>(getString(R.string.activity_start_message10), new TimeOfTheDayBuilder(dateTimeFormatter), new DayOfTheWeekBuilder()));
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

    /**
     * Return repository of messages to display in the activity start events view.
     *
     * @return repository of activity start events related messages
     */
    public MessageRepository<LocalDateTime> getActivityMessagesRepository() {
        return activityMessagesRepository;
    }

}
