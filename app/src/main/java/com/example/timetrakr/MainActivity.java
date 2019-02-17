package com.example.timetrakr;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.MenuItem;

import com.example.timetrakr.view.activities.durations.ActivitiesDurationsFragment;
import com.example.timetrakr.view.activities.started.ActivitiesStartedFragment;

/**
 * Main activity of the application, that contains bottom navigation and a fragment container.
 */
public class MainActivity extends AppCompatActivity {

    private Integer selectedItemId;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationListener listener = new BottomNavigationListener(getSupportFragmentManager(), R.id.main_fragment_container);
        listener.addFragment(R.id.navigation_activity_start_events, new ActivitiesStartedFragment());
        listener.addFragment(R.id.navigation_activity_durations, new ActivitiesDurationsFragment());
        navigation.setOnNavigationItemSelectedListener(listener);
        if (selectedItemId == null) {
            selectedItemId = R.id.navigation_activity_start_events;
        }
        navigation.setSelectedItemId(selectedItemId);
    }

    /**
     * Bottom navigation item selection listener.
     */
    private class BottomNavigationListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        private FragmentManager fragmentManager;
        private int fragmentContainerId;
        private SparseArray<Fragment> navigationItemIdToFragment;

        /**
         * Construct the listener.
         *
         * @param fragmentManager fragment manager used to display fragments
         * @param fragmentContainerId id of the fragment container to display fragments in
         */
        public BottomNavigationListener(FragmentManager fragmentManager, int fragmentContainerId) {
            navigationItemIdToFragment = new SparseArray<>();
            this.fragmentManager = fragmentManager;
            this.fragmentContainerId = fragmentContainerId;
        }

        /**
         * Show specified fragment in the fragment container when user selects menu item with
         * the specified ID in the bottom navigation.
         *
         * @param menuItemId ID of the menu item
         * @param fragment fragment to display
         */
        public void addFragment(int menuItemId, Fragment fragment) {
            navigationItemIdToFragment.put(menuItemId, fragment);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragmentToDisplay = navigationItemIdToFragment.get(menuItem.getItemId());
            if (fragmentToDisplay != null) {
                fragmentManager
                        .beginTransaction()
                        .replace(fragmentContainerId, fragmentToDisplay)
                        .commit();
                return true;
            } else {
                return false;
            }
        }
    }

}
