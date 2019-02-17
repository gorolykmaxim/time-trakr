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

    /**
     * Construct the view holder.
     *
     * @param itemView parent view group of this view holder
     * @param durationFormatter duration formatter used to format duration of the displayed activity
     */
    public ActivityDurationViewHolder(@NonNull View itemView, DurationFormatter durationFormatter) {
        super(itemView);
        ViewGroup viewGroup = (ViewGroup)itemView;
        activityNameTextView = viewGroup.findViewById(R.id.activity_view_activity_text);
        durationTextView = viewGroup.findViewById(R.id.activity_view_duration_text);
        this.durationFormatter = durationFormatter;
    }

    /**
     * Update view state to display information about the specified activity duration.
     *
     * @param activityDuration activity duration to display in the view
     */
    public void update(ActivityDuration activityDuration) {
        activityNameTextView.setText(activityDuration.getActivityName());
        durationTextView.setText(durationFormatter.format(activityDuration.getDuration()));
    }

}
