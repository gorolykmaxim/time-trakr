package com.example.timetrakr.model.activity.events;

import java.time.LocalDateTime;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

/**
 * Event that signalises that a user has started a new activity.
 */
@Entity(tableName = "activity_start_event", primaryKeys = {"start_date"})
public class ActivityStartEvent {

    @ColumnInfo(name = "activity_name")
    @NonNull
    private String activityName;
    @ColumnInfo(name = "start_date")
    @NonNull
    private LocalDateTime startDate;

    /**
     * Construct activity start event.
     *
     * @param activityName name of the started activity
     * @param startDate date when the activity has been started
     */
    public ActivityStartEvent(String activityName, LocalDateTime startDate) {
        this.activityName = activityName;
        this.startDate = startDate;
    }

    /**
     * Return name of the activity.
     *
     * @return name of the activity
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * Return the date when the activity has been started.
     *
     * @return activity start date
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityStartEvent that = (ActivityStartEvent) o;
        return Objects.equals(activityName, that.activityName) &&
                Objects.equals(startDate, that.startDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(activityName, startDate);
    }
}
