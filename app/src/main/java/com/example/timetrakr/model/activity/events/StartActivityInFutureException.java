package com.example.timetrakr.model.activity.events;

import java.time.LocalDateTime;

/**
 * User tried to start activity with a start date, that is later than current date.
 */
public class StartActivityInFutureException extends Exception {

    private LocalDateTime currentDate, specifiedDate;

    /**
     * Construct an exception.
     *
     * @param specifiedDate date specified by the user
     * @param currentDate current date
     */
    public StartActivityInFutureException(LocalDateTime specifiedDate, LocalDateTime currentDate) {
        super(String.format("Cannot start activity in the future. It is %s right now, and user tried to specify %s.", currentDate, specifiedDate));
        this.specifiedDate = specifiedDate;
        this.currentDate = currentDate;
    }

    /**
     * Get the date when the attempt was made.
     *
     * @return date when the exception has happened
     */
    public LocalDateTime getCurrentDate() {
        return currentDate;
    }

    /**
     * Get the date specified by the user.
     *
     * @return date specified by the user
     */
    public LocalDateTime getSpecifiedDate() {
        return specifiedDate;
    }

}
