package com.example.timetrakr.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.timetrakr.model.activity.events.ActivityStartEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO of {@link ActivityStartEvent} table.
 */
@Dao
public interface ActivityStartEventDao {

    /**
     * Return all activity start events, made after the specified time.
     *
     * <p>All events are returned in ascending order of their creation time.</p>
     *
     * @param time time to search for events with
     * @return activity start events, made after the specified time
     */
    @Query("SELECT * FROM activity_start_event WHERE start_date > :time ORDER BY start_date ASC")
    LiveData<List<ActivityStartEvent>> getObservableForAllStartedBefore(LocalDateTime time);

    /**
     * Return all activity start events, made after the specified time which have a name, that is
     * approximately equal to the specified one.
     *
     * @param time time to search for events with
     * @param name approximate activity name
     * @return all activity start events, made after the specified time which have a name, that is
     * approximately equal to the specified one
     */
    @Query("SELECT * FROM activity_start_event WHERE start_date > :time AND activity_name LIKE :name ORDER BY start_date ASC")
    List<ActivityStartEvent> getAllStartedBeforeWithNameLike(LocalDateTime time, String name);

    /**
     * Insert specified activity start event.
     *
     * @param activityStartEvent activity start event to insert
     */
    @Insert
    void insert(ActivityStartEvent activityStartEvent);

    /**
     * Delete specified activity start event.
     *
     * @param activityStartEvent activity start event to delete
     */
    @Delete
    void delete(ActivityStartEvent activityStartEvent);

}
