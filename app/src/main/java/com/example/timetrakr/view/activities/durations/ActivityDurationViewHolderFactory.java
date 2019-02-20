package com.example.timetrakr.view.activities.durations;

import android.view.View;

import com.example.timetrakr.common.DurationFormatter;

/**
 * Factory of {@link ActivityDurationViewHolder} instances.
 */
public class ActivityDurationViewHolderFactory {

    private DurationFormatter formatter;
    private ActivityDurationViewHolder.ColorSet selectedColorSet, notSelectedColorSet;

    /**
     * Construct factory.
     *
     * @param formatter formatter used by constructed view holders to format activity durations
     * @param selectedColorSet colors to paint created view holders into when they are selected
     * @param notSelectedColorSet colors to paint created view holders into when they are not selected
     */
    public ActivityDurationViewHolderFactory(DurationFormatter formatter,
                                             ActivityDurationViewHolder.ColorSet selectedColorSet,
                                             ActivityDurationViewHolder.ColorSet notSelectedColorSet) {
        this.formatter = formatter;
        this.selectedColorSet = selectedColorSet;
        this.notSelectedColorSet = notSelectedColorSet;
    }

    /**
     * Create view holder.
     *
     * @param view parent view group of the view holder
     * @return created view holder
     */
    public ActivityDurationViewHolder create(View view) {
        return new ActivityDurationViewHolder(view, formatter, selectedColorSet, notSelectedColorSet);
    }

}
