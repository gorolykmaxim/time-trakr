package com.example.timetrakr.view.activities.started;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.timetrakr.R;
import com.example.timetrakr.model.activity.events.NewActivityNameIsTooShortException;
import com.example.timetrakr.view.activities.started.dialog.StartActivityDialogFragment;
import com.example.timetrakr.view.common.recycler.LeftRightCallback;
import com.example.timetrakr.viewmodel.activities.started.ActivitiesStartedViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Displays list of activity start events, created today.
 */
public class ActivitiesStartedFragment extends Fragment {

    private static final String START_ACTIVITY_DIALOG = "start_activity_dialog";

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.activities_started_view, container, false);
        /*
        Obtain reference to the view model.
         */
        ActivitiesStartedViewModel viewModel = ViewModelProviders.of(this).get(ActivitiesStartedViewModel.class);
        /*
        Create date-time formatter to format all dates displayed in the fragment.
         */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        /*
        Initialize "start new activity" button.
         */
        FloatingActionButton startActivityButton = root.findViewById(R.id.open_start_new_activity_dialog);
        // Prepare listener for case when created activity name is too short.
        Consumer<NewActivityNameIsTooShortException> newActivityNameIsTooShortListener = e -> {
            String message = String.format(getString(R.string.activity_name_too_short), e.getExpectedMinimalLength());
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        };
        // Open bottom sheet dialog to create new activity when clicking on a FAB.
        View.OnClickListener startActivityButtonListener = v -> {
            StartActivityDialogFragment startActivityDialogFragment = StartActivityDialogFragment.create(getString(R.string.since), formatter);
            startActivityDialogFragment.setListener((activityName, startDate) -> {
                viewModel.startNewActivity(activityName, startDate, newActivityNameIsTooShortListener);
            });
            startActivityDialogFragment.show(getChildFragmentManager(), START_ACTIVITY_DIALOG);
        };
        startActivityButton.setOnClickListener(startActivityButtonListener);
        /*
        Initialize recycler view that displays all started activities.
         */
        RecyclerView recyclerView = root.findViewById(R.id.activities_started_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Setup recycler view adapter.
        ActivityStartViewHolderFactory viewHolderFactory = new ActivityStartViewHolderFactory(getString(R.string.since), formatter);
        ActivitiesStartEventsAdapter adapter = new ActivitiesStartEventsAdapter(viewHolderFactory);
        recyclerView.setAdapter(adapter);
        // Update recycler view adapter when list of today's activity start events is updated.
        viewModel.getActivityStartEvents().observe(this, adapter::updateActivityStartEvents);
        // Prepare item touch helper for swipe gestures on recycler view.
        LeftRightCallback callback = new LeftRightCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        // Enable swipe left to delete activity.
        Drawable icon = getResources().getDrawable(R.drawable.ic_delete_forever_black_24dp, null);
        ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.deleteRed, null));
        LeftRightCallback.OnSwipeListener listener = viewHolder -> {
            ActivityStartViewHolder holder = (ActivityStartViewHolder)viewHolder;
            String name = holder.getActivityName();
            LocalDateTime dateTime = holder.getStartDate();
            String warning = String.format(getString(R.string.delete_activity_warning), name, dateTime.format(formatter));
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage(warning)
                    .setPositiveButton(R.string.delete, (dlg, id) -> viewModel.deleteActivityStart(name, dateTime.toString()))
                    .setNegativeButton(R.string.cancel, (dlg, id) -> adapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                    .create();
            dialog.show();
        };
        callback.setLeftSwipe(icon, background, listener);
        // Enable swipe right to prolong existing activity.
        icon = getResources().getDrawable(R.drawable.ic_autorenew_black_24dp, null);
        background = new ColorDrawable(getResources().getColor(R.color.copyGreen, null));
        listener = viewHolder -> {
            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            ActivityStartViewHolder holder = (ActivityStartViewHolder)viewHolder;
            String name = holder.getActivityName();
            StartActivityDialogFragment startActivityDialogFragment = StartActivityDialogFragment.create(getString(R.string.since), formatter);
            startActivityDialogFragment.setActivityName(name);
            startActivityDialogFragment.setListener((activityName, startDate) -> {
                viewModel.startNewActivity(activityName, startDate, newActivityNameIsTooShortListener);
            });
            startActivityDialogFragment.show(getChildFragmentManager(), START_ACTIVITY_DIALOG);
        };
        callback.setRightSwipe(icon, background, listener);
        return root;
    }

}