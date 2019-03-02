package com.example.timetrakr.view.activities.durations;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timetrakr.R;
import com.example.timetrakr.common.DurationFormatter;
import com.example.timetrakr.view.common.ClosableTextView;
import com.example.timetrakr.viewmodel.activities.durations.ActivitiesDurationViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        TextView titleMessage = root.findViewById(R.id.title_message);
        ClosableTextView totalDurationView = root.findViewById(R.id.total_duration);
        RecyclerView recyclerView = root.findViewById(R.id.activities_durations_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DurationFormatter formatter = new DurationFormatter();

        Context context = root.getContext();
        int selectedNameColor = context.getColor(R.color.colorWhite);
        int selectedDurationColor = context.getColor(R.color.colorWhite);
        int selectedBackgroundColor = context.getColor(R.color.colorPrimary);
        int notSelectedNameColor = context.getColor(android.R.color.black);
        int notSelectedDurationColor = context.getColor(R.color.colorAccent);
        int notSelectedBackgroundColor = context.getColor(R.color.colorWhite);
        ActivityDurationViewHolder.ColorSet selectedColorSet = new ActivityDurationViewHolder.ColorSet(selectedNameColor, selectedDurationColor, selectedBackgroundColor);
        ActivityDurationViewHolder.ColorSet notSelectedColorSet = new ActivityDurationViewHolder.ColorSet(notSelectedNameColor, notSelectedDurationColor, notSelectedBackgroundColor);
        ActivityDurationViewHolderFactory factory = new ActivityDurationViewHolderFactory(formatter, selectedColorSet, notSelectedColorSet);
        ActivitiesDurationsAdapter adapter = new ActivitiesDurationsAdapter(factory);
        recyclerView.setAdapter(adapter);
        viewModel = ViewModelProviders.of(this).get(ActivitiesDurationViewModel.class);
        viewModel.getActivityDurations().observe(this, adapter::updateActivityDurations);
        viewModel.getObservableSelectedActivities().observe(this, adapter::updateSelectedActivityDurations);
        adapter.setOnItemSelectListener(((viewHolder, isSelected) -> {
            if (isSelected) {
                viewModel.deselectActivityDuration(viewHolder.getActivityDuration());
            } else {
                viewModel.selectActivityDuration(viewHolder.getActivityDuration());
            }
        }));
        String totalDurationTemplate = context.getString(R.string.total_duration);
        viewModel.getObservableTotalDuration().observe(this, totalDuration -> {
            totalDurationView.setText(String.format(totalDurationTemplate, formatter.format(totalDuration)));
            if (totalDuration.isZero()) {
                totalDurationView.hide();
            } else {
                totalDurationView.show();
            }
        });
        viewModel.getObservableMessage().observe(this, titleMessage::setText);
        totalDurationView.setOnCloseListener(viewModel::clearSelectedActivityDurations);
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
