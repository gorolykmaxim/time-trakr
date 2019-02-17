package com.example.timetrakr.persistence;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.timetrakr.model.activity.events.ActivityStartEvent;

/**
 * Database of the time-trakr app.
 */
@Database(entities = {ActivityStartEvent.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class TimeTrakrDatabase extends RoomDatabase {

    /**
     * Name of the logical database.
     */
    public static final String NAME = "time_trakr";

    /**
     * Return DAO to the table used to store activity start events.
     *
     * @return DAO of activity start events
     */
    public abstract ActivityStartEventDao getActivityStartEventDao();

}
