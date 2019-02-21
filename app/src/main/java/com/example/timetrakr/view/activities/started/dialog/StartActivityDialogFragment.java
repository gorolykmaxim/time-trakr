package com.example.timetrakr.view.activities.started.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.timetrakr.R;
import com.example.timetrakr.view.common.TimeEdit;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Dialog fragment, that allows user to start new activities.
 */
public class StartActivityDialogFragment extends BottomSheetDialogFragment {

    private State state;
    private DateTimeFormatter formatter;
    private DialogInterface.OnCancelListener onCancelListener;

    /**
     * Construct the dialog.
     */
    public StartActivityDialogFragment() {
        state = new State(this);
        state.setStartDate(LocalDateTime.now());
    }

    /**
     * Create new dialog instance.
     *
     * @param formatter formatter used to format displayer start date-time of the activity
     * @return create dialog instance
     */
    public static StartActivityDialogFragment create(DateTimeFormatter formatter) {
        StartActivityDialogFragment dialogFragment = new StartActivityDialogFragment();
        dialogFragment.formatter = formatter;
        return dialogFragment;
    }

    /**
     * Specify a listener to be called when dialog gets cancelled.
     *
     * @param onCancelListener listener to call
     */
    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    /**
     * Specify listener to be called when user saves newly created activity.
     *
     * @param listener listener to call
     */
    public void setOntStartActivityListener(OnStartActivityListener listener) {
        state.setListener(listener);
    }

    /**
     * Specify an activity name to be displayed when user opens up a dialog.
     *
     * @param activityName activity name to display
     */
    public void setActivityName(String activityName) {
        state.setActivityName(activityName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialog);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.start_activity_dialog, container, false);
        EditText activityNameEditText = viewGroup.findViewById(R.id.start_activity_dialog_activity_name);
        String activityName = state.getActivityName();
        LocalDateTime startDate = state.getStartDate();
        if (activityName != null && !activityName.isEmpty()) {
            activityNameEditText.setText(activityName);
        }
        activityNameEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (event == null) {
                state.start();
                return true;
            } else {
                return false;
            }
        });
        activityNameEditText.addTextChangedListener(new ActivityNameListener(state));
        TimeEdit timeEdit = viewGroup.findViewById(R.id.start_activity_dialog_time);
        timeEdit.setFormatter(formatter);
        timeEdit.update(startDate);
        timeEdit.setOnDateChangeListener(state::setStartDate);
        Button startButton = viewGroup.findViewById(R.id.start_activity_dialog_start_button);
        startButton.setOnClickListener(v -> state.start());
        return viewGroup;
    }

    /**
     * State of the activity start dialog.
     *
     * <p>Responsible for tracking a state of information, necessary for a new activity creation,
     * and actual creation triggering when all necessary information is collected.</p>
     *
     * <p>While triggering new activity creation, the state also closes it's owner-dialog.</p>
     *
     */
    private class State {

        private StartActivityDialogFragment.OnStartActivityListener listener;
        private String activityName;
        private LocalDateTime startDate;
        private StartActivityDialogFragment dialogFragment;

        /**
         * Construct an initial state.
         *
         * @param dialogFragment owner of the state
         */
        public State(StartActivityDialogFragment dialogFragment) {
            this.dialogFragment = dialogFragment;
        }

        /**
         * Get name of the activity being created.
         *
         * @return name of the new activity
         */
        public String getActivityName() {
            return activityName;
        }

        /**
         * Get start date-time of the activity being created.
         *
         * @return start date-time
         */
        public LocalDateTime getStartDate() {
            return startDate;
        }

        /**
         * Set listener to be called, when activity creation triggers.
         *
         * @param listener listener to be called
         */
        public void setListener(StartActivityDialogFragment.OnStartActivityListener listener) {
            this.listener = listener;
        }

        /**
         * Set name of the activity being created.
         *
         * @param activityName name of the activity
         */
        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        /**
         * Set start date-time of the activity being created.
         *
         * @param startDate start date-time
         */
        public void setStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        /**
         * Trigger new activity creation and close the owner-dialog.
         */
        public void start() {
            if (listener != null) {
                listener.onStartActivity(activityName, startDate);
            }
            dialogFragment.dismiss();
        }

    }

    /**
     * A listener for a activity name change.
     */
    private class ActivityNameListener implements TextWatcher {

        private State dialogState;

        /**
         * Construct a listener.
         *
         * @param dialogState state of a dialog to notify about a name change.
         */
        public ActivityNameListener(State dialogState) {
            this.dialogState = dialogState;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            dialogState.setActivityName(s.toString());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void afterTextChanged(Editable s) {

        }

    }

    /**
     * New activity start listener.
     */
    public interface OnStartActivityListener {

        /**
         * Called when dialog tries to save new activity.
         *
         * @param activityName name of the created activity
         * @param startDate start date-time of the created activity
         */
        void onStartActivity(String activityName, LocalDateTime startDate);
    }

}
