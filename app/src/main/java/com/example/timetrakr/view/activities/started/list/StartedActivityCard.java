package com.example.timetrakr.view.activities.started.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.timetrakr.R;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;

import java.time.format.DateTimeFormatter;

public class StartedActivityCard extends CardView {
    private String displayFormat;
    private DateTimeFormatter formatter;
    private TextView activityNameView;
    private TextView activityStartTimeView;
    private ActivityStartEvent activityStartEvent;

    public StartedActivityCard(@NonNull Context context) {
        this(context, null);
    }

    public StartedActivityCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.style.CardBottomMargin);
    }

    public StartedActivityCard(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.started_activity_card, this);
        displayFormat = getResources().getString(R.string.since);
        formatter = DateTimeFormatter.ofPattern(getResources().getString(R.string.date_time_format));
        activityNameView = findViewById(R.id.started_activity_card_activity_name);
        activityStartTimeView = findViewById(R.id.started_activity_card_activity_start_time);
        setElevation(0);
        setLayoutParams(new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setActivityStartEvent(ActivityStartEvent activityStartEvent) {
        this.activityStartEvent = activityStartEvent;
        activityNameView.setText(activityStartEvent.getActivityName());
        activityStartTimeView.setText(String.format(displayFormat, activityStartEvent.getStartDate().format(formatter)));
    }

    public ActivityStartEvent getActivityStartEvent() {
        return activityStartEvent;
    }
}
