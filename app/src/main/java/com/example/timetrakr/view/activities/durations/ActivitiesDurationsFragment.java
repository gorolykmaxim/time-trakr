package com.example.timetrakr.view.activities.durations;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timetrakr.R;
import com.example.timetrakr.common.DurationFormatter;
import com.example.timetrakr.viewmodel.activities.durations.ActivitiesDurationViewModel;

/**
 * Displays overall durations of today's user's activities.
 */
public class ActivitiesDurationsFragment extends Fragment {

    private ActivitiesDurationViewModel viewModel;

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.activities_durations_view, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.activities_durations_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DurationFormatter formatter = new DurationFormatter();
        ActivityDurationViewHolderFactory factory = new ActivityDurationViewHolderFactory(formatter);
        ActivitiesDurationsAdapter adapter = new ActivitiesDurationsAdapter(factory);
        recyclerView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(ActivitiesDurationViewModel.class);
        viewModel.getActivityDurations().observe(this, adapter::updateActivityDurations);
        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        super.onStart();
        // Any time the fragment is restarted (after screen unlock for example) we need to
        // recalculate durations displayed in the fragment, so they are up-to-date.
        viewModel.recalculateActivityDurations();
    }
}
