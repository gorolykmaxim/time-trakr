package com.example.timetrakr.model.activity.events;

import android.database.sqlite.SQLiteConstraintException;

import com.example.timetrakr.persistence.ActivityStartEventDao;

import java.time.LocalDate;
import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * Repository of {@link ActivityStartEvent}
 */
public class ActivityStartEventRepository {

    private ActivityStartEventDao dao;

    /**
     * Construct activity start events repository.
     *
     * @param dao DAO to access activity start event database table
     */
    public ActivityStartEventRepository(ActivityStartEventDao dao) {
        this.dao = dao;
    }

    /**
     * Return list of all activity start events, that have been made today.
     *
     * @return list of all activity start events started today
     */
    public LiveData<List<ActivityStartEvent>> getObservableForAllForToday() {
        return dao.getObservableForAllStartedBefore(LocalDate.now().atStartOfDay());
    }

    /**
     * Return list of all activity start events, that have been made today and activity names
     * of which are approximately equal to the specified one.
     *
     * @param activityName approximate activity name to match all activity start events against
     * @return list of all activity start events started today with the approximate specified name
     */
    public List<ActivityStartEvent> findAllForTodayWithNameLike(String activityName) {
        return dao.getAllStartedBeforeWithNameLike(LocalDate.now().atStartOfDay(), String.format("%%%s%%", activityName));
    }

    /**
     * Save specified activity start event in the repository.
     *
     * @param activityStartEvent activity start event to save
     * @throws AnotherActivityAlreadyStartedException in case another activity has been already
     * started at the time, specified activity is started
     */
    public void save(ActivityStartEvent activityStartEvent) throws AnotherActivityAlreadyStartedException {
        try {
            dao.insert(activityStartEvent);
        } catch (SQLiteConstraintException e) {
            ActivityStartEvent alreadyStartedActivity = dao.getByStartDate(activityStartEvent.getStartDate());
            throw new AnotherActivityAlreadyStartedException(alreadyStartedActivity);
        }
    }

    /**
     * Delete specified activity start event from the repository.
     *
     * @param activityStartEvent activity start event to delete
     */
    public void delete(ActivityStartEvent activityStartEvent) {
        dao.delete(activityStartEvent);
    }

}
