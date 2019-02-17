package com.example.timetrakr.view.activities.started;

import android.view.View;

import java.time.format.DateTimeFormatter;

/**
 * Factory of {@link ActivityStartViewHolder} instances.
 */
public class ActivityStartViewHolderFactory {

    private String startTimeTemplate;
    private DateTimeFormatter formatter;

    /**
     * Construct the factory.
     *
     * @param startTimeTemplate template, used by view holders to display activity start time
     * @param formatter formatter, used by view holders to format activity start time
     */
    public ActivityStartViewHolderFactory(String startTimeTemplate, DateTimeFormatter formatter) {
        this.startTimeTemplate = startTimeTemplate;
        this.formatter = formatter;
    }

    /**
     * Create view holder for the specified parent view.
     *
     * @param view parent view of the view holder
     * @return view holder
     */
    public ActivityStartViewHolder create(View view) {
        return new ActivityStartViewHolder(view, startTimeTemplate, formatter);
    }

}
