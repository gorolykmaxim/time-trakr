package com.example.timetrakr.view.activities.durations;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timetrakr.R;
import com.example.timetrakr.common.DurationFormatter;
import com.example.timetrakr.model.activity.duration.ActivityDuration;

/**
 * View holder used to display information about duration of a single activity.
 */
public class ActivityDurationViewHolder extends RecyclerView.ViewHolder {

    private TextView activityNameTextView, durationTextView;
    private DurationFormatter durationFormatter;
    private ColorSet selectedColorSet, notSelectedColorSet;
    private ActivityDuration activityDuration;

    /**
     * Construct the view holder.
     *
     * @param itemView parent view group of this view holder
     * @param durationFormatter duration formatter used to format duration of the displayed activity
     * @param selectedColorSet colors to paint view holder into, when view holder is selected
     * @param notSelectedColorSet colors to paint view holder into, when view holder is not selected
     */
    public ActivityDurationViewHolder(@NonNull View itemView, DurationFormatter durationFormatter,
                                      ColorSet selectedColorSet, ColorSet notSelectedColorSet) {
        super(itemView);
        ViewGroup viewGroup = (ViewGroup)itemView;
        activityNameTextView = viewGroup.findViewById(R.id.activity_view_activity_text);
        durationTextView = viewGroup.findViewById(R.id.activity_view_duration_text);
        this.durationFormatter = durationFormatter;
        this.selectedColorSet = selectedColorSet;
        this.notSelectedColorSet = notSelectedColorSet;
    }

    /**
     * Update view state to display information about the specified activity duration.
     *
     * @param activityDuration activity duration to display in the view
     */
    public void update(ActivityDuration activityDuration) {
        this.activityDuration = activityDuration;
        activityNameTextView.setText(activityDuration.getActivityName());
        durationTextView.setText(durationFormatter.format(activityDuration.getDuration()));
    }

    /**
     * Get activity duration, displayed in this view holder.
     *
     * @return activity duration of this view holder
     */
    public ActivityDuration getActivityDuration() {
        return activityDuration;
    }

    /**
     * Select this view holder and change it's appearance accordingly.
     */
    public void select() {
        selectedColorSet.paint(activityNameTextView, durationTextView, itemView);
    }

    /**
     * Clear selection of this view holder and change it's appearance back to normal.
     */
    public void deselect() {
        notSelectedColorSet.paint(activityNameTextView, durationTextView, itemView);
    }

    /**
     * Specify listener that will be called when user clicks on this view holder.
     *
     * @param onClickListener listener to call
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        itemView.setOnClickListener(onClickListener);
    }

    /**
     * Set of colors, used by {@link ActivityDurationViewHolder} depending on it's selection state.
     */
    public static class ColorSet {

        private int nameColor, durationColor, backgroundColor;

        /**
         * Create a color set.
         *
         * @param nameColor ID of the activity name text color
         * @param durationColor ID of the activity duration color
         * @param backgroundColor ID of the background color of a view holder
         */
        public ColorSet(int nameColor, int durationColor, int backgroundColor) {
            this.nameColor = nameColor;
            this.durationColor = durationColor;
            this.backgroundColor = backgroundColor;
        }

        /**
         * Paint specified parts of the view holder into their colors, specified in this color set.
         *
         * @param name activity name text view to paint
         * @param duration activity duration text view to paint
         * @param parent parent view (which is a background) to paint
         */
        public void paint(TextView name, TextView duration, View parent) {
            name.setTextColor(nameColor);
            duration.setTextColor(durationColor);
            parent.setBackgroundColor(backgroundColor);
        }

    }

}
