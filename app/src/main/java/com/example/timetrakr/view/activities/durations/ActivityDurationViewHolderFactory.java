package com.example.timetrakr.view.activities.durations;

import android.view.View;

import com.example.timetrakr.common.DurationFormatter;

/**
 * Factory of {@link ActivityDurationViewHolder} instances.
 */
public class ActivityDurationViewHolderFactory {

    private DurationFormatter formatter;

    /**
     * Construct factory.
     *
     * @param formatter formatter used by constructed view holders to format activity durations
     */
    public ActivityDurationViewHolderFactory(DurationFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * Create view holder.
     *
     * @param view parent view group of the view holder
     * @return created view holder
     */
    public ActivityDurationViewHolder create(View view) {
        return new ActivityDurationViewHolder(view, formatter);
    }

}
