package com.example.timetrakr.view.activities.started;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.timetrakr.R;
import com.example.timetrakr.model.activity.events.ActivityStartEvent;
import com.example.timetrakr.view.activities.started.dialog.StartActivityDialogFragment;
import com.example.timetrakr.view.activities.started.list.ActivitiesStartEventsAdapter;
import com.example.timetrakr.view.activities.started.list.StartedActivityCard;
import com.example.timetrakr.view.common.recycler.EmptiableRecyclerView;
import com.example.timetrakr.view.common.recycler.LeftRightCallback;
import com.example.timetrakr.viewmodel.activities.started.ActivitiesStartedViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gorolykmaxim.android.commons.dialog.DialogServant;
import com.gorolykmaxim.android.commons.recyclerview.GenericViewHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Displays list of activity start events, created today.
 */
public class ActivitiesStartedFragment extends Fragment {
    private StartActivityDialogFragment startActivityDialog;
    private DialogServant dialogServant = new DialogServant();

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(getString(R.string.date_time_format));
        /*
        Initialize "start new activity" button.
         */
        FloatingActionButton startActivityButton = root.findViewById(R.id.open_start_new_activity_dialog);
        // Prepare listener for case when created activity name is too short.
        viewModel.getNameIsTooShortObservable().observe(this, e -> {
            String message = String.format(getString(R.string.activity_name_too_short), e.getExpectedMinimalLength());
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        });
        // Prepare listener for case when user tries to create activity at the date, when another
        // activity has been started.
        viewModel.getActivityAlreadyStartedObservable().observe(this, e -> {
            ActivityStartEvent anotherActivity = e.getAnotherActivity();
            String message = String.format(getString(R.string.activity_already_started), anotherActivity.getActivityName(), anotherActivity.getStartDate().format(formatter));
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        });
        // Prepare listener for case when created activity start date is in the future.
        viewModel.getStartInFutureObservable().observe(this, e -> {
            String message = String.format(getString(R.string.start_activity_in_future), e.getCurrentDate().format(formatter), e.getSpecifiedDate().format(formatter));
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        });
        // Initialize start activity dialog displayer.
        startActivityDialog = new StartActivityDialogFragment();
        startActivityDialog.setOntStartActivityListener(viewModel::startNewActivity);
        // Open bottom sheet dialog to create new activity when clicking on a FAB.
        startActivityButton.setOnClickListener(v -> dialogServant.showIfNotShown(startActivityDialog, getChildFragmentManager()));
        /*
        Initialize recycler view that displays all started activities.
         */
        EmptiableRecyclerView<ActivityStartEvent> recyclerView = root.findViewById(R.id.activities_started_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Setup recycler view adapter.
        ActivitiesStartEventsAdapter adapter = new ActivitiesStartEventsAdapter();
        recyclerView.setListAdapter(adapter);
        // Update recycler view adapter when list of today's activity start events is updated.
        viewModel.getActivityStartEvents().observe(this, recyclerView::displayList);
        viewModel.getObservableMessage().observe(this, recyclerView::setEmptyListPlaceholderText);
        // Prepare item touch helper for swipe gestures on recycler view.
        LeftRightCallback callback = new LeftRightCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        recyclerView.setItemTouchHelper(itemTouchHelper);
        // Enable swipe left to delete activity.
        Drawable icon = getResources().getDrawable(R.drawable.ic_delete_forever_black_24dp, null);
        ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.deleteRed, null));
        LeftRightCallback.OnSwipeListener listener = viewHolder -> {
            StartedActivityCard view = GenericViewHolder.getViewOf(viewHolder);
            ActivityStartEvent activityStartEvent = view.getActivityStartEvent();
            String name = activityStartEvent.getActivityName();
            LocalDateTime dateTime = activityStartEvent.getStartDate();
            String warning = String.format(getString(R.string.delete_activity_warning), name, dateTime.format(formatter));
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage(warning)
                    .setPositiveButton(R.string.delete, (dlg, id) -> viewModel.deleteActivityStart(name, dateTime.toString()))
                    .setNegativeButton(R.string.cancel, (dlg, id) -> dlg.cancel())
                    .create();
            dialog.setOnCancelListener(dlg -> adapter.notifyItemChanged(viewHolder.getAdapterPosition()));
            dialog.setCancelable(true);
            dialog.show();
        };
        callback.setLeftSwipe(icon, background, listener);
        // Enable swipe right to prolong existing activity.
        icon = getResources().getDrawable(R.drawable.ic_autorenew_black_24dp, null);
        background = new ColorDrawable(getResources().getColor(R.color.copyGreen, null));
        listener = viewHolder -> {
            adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            StartedActivityCard view = GenericViewHolder.getViewOf(viewHolder);
            ActivityStartEvent activityStartEvent = view.getActivityStartEvent();
            startActivityDialog.setDefaultActivityName(activityStartEvent.getActivityName());
            dialogServant.showIfNotShown(startActivityDialog, getChildFragmentManager());
        };
        callback.setRightSwipe(icon, background, listener);
        return root;
    }

}
