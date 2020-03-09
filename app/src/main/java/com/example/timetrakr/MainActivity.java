package com.example.timetrakr;

import android.os.Bundle;

import com.example.timetrakr.view.activities.durations.ActivitiesDurationsFragment;
import com.example.timetrakr.view.activities.started.ActivitiesStartedFragment;
import com.gorolykmaxim.android.commons.bottomnavigation.BottomNavigationActivity;

/**
 * Main activity of the application, that contains bottom navigation and a fragment container.
 */
public class MainActivity extends BottomNavigationActivity {

    public MainActivity() {
        super(R.menu.navigation, R.id.navigation_activity_start_events);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        addFragment(R.id.navigation_activity_start_events, new ActivitiesStartedFragment());
        addFragment(R.id.navigation_activity_durations, new ActivitiesDurationsFragment());
        super.onCreate(savedInstanceState);
    }
}
