package com.example.timetrakr.view.activities.started;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timetrakr.R;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * View holder that displays information about a single activity start event (e.g. an activity name,
 * and a start time).
 */
public class ActivityStartViewHolder extends RecyclerView.ViewHolder {

    private String startTimeTemplate, activityName;
    private LocalDateTime startDate;
    private TextView primaryTextView, secondaryTextView;
    private DateTimeFormatter formatter;


    /**
     * Construct the view holder.
     *
     * @param itemView parent view group
     * @param startTimeTemplate string template, used to display the start time of the activity
     * @param formatter date-time formatter, used to format the start date
     */
    public ActivityStartViewHolder(@NonNull View itemView, String startTimeTemplate, DateTimeFormatter formatter) {
        super(itemView);
        ViewGroup viewGroup = (ViewGroup)itemView;
        primaryTextView = viewGroup.findViewById(R.id.deletable_activity_view_primary_text);
        secondaryTextView = viewGroup.findViewById(R.id.deletable_activity_view_secondary_text);
        this.formatter = formatter;
        this.startTimeTemplate = startTimeTemplate;
    }

    /**
     * Update view holder's state and display inforamtion about the specified activity start event.
     *
     * @param activityStartEvent event to display
     */
    public void update(ActivityStartEvent activityStartEvent) {
        activityName = activityStartEvent.getActivityName();
        startDate = activityStartEvent.getStartDate();
        primaryTextView.setText(activityStartEvent.getActivityName());
        String rightText = String.format(startTimeTemplate, activityStartEvent.getStartDate().format(formatter));
        secondaryTextView.setText(rightText);
    }

    /**
     * Return name of the activity, displayed by the view holder.
     *
     * @return name of the activity or null if view holder does not display any activity start event
     * information yet
     */
    public String getActivityName() {
        return activityName;
    }

    /**
     * Return start date of the activity, displayed by the view holder.
     *
     * @return start date of the activity or null if view holder does not display any activity
     * start event information yet
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }
}
