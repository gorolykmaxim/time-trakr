package com.example.timetrakr.view.activities.started.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.timetrakr.R;
import com.example.timetrakr.view.common.DateEditView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class StartActivityDialogFragment extends BottomSheetDialogFragment {

    private State state;
    private DateEditView startDateEditView;

    public StartActivityDialogFragment() {
        startDateEditView = new DateEditView();
        state = new State(this);
        state.setStartDate(LocalDateTime.now());
    }

    public static StartActivityDialogFragment create(String startTimeTemplate, DateTimeFormatter formatter) {
        StartActivityDialogFragment dialogFragment = new StartActivityDialogFragment();
        dialogFragment.startDateEditView.setStartTimeTemplate(startTimeTemplate);
        dialogFragment.startDateEditView.setFormatter(formatter);
        return dialogFragment;
    }

    public void setStartDateEditView(DateEditView startDateEditView) {
        this.startDateEditView = startDateEditView;
    }

    public void setListener(OnStartActivityListener listener) {
        state.setListener(listener);
    }

    public void setActivityName(String activityName) {
        state.setActivityName(activityName);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

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
        startDateEditView.setTextView(viewGroup.findViewById(R.id.start_activity_dialog_time));
        startDateEditView.update(startDate);
        startDateEditView.setOnDateChangeListener(state::setStartDate);
        Button startButton = viewGroup.findViewById(R.id.start_activity_dialog_start_button);
        startButton.setOnClickListener(v -> state.start());
        return viewGroup;
    }

    private class State {

        private StartActivityDialogFragment.OnStartActivityListener listener;
        private String activityName;
        private LocalDateTime startDate;
        private StartActivityDialogFragment dialogFragment;

        public State(StartActivityDialogFragment dialogFragment) {
            this.dialogFragment = dialogFragment;
        }

        public String getActivityName() {
            return activityName;
        }

        public LocalDateTime getStartDate() {
            return startDate;
        }

        public void setListener(StartActivityDialogFragment.OnStartActivityListener listener) {
            this.listener = listener;
        }

        public void setActivityName(String activityName) {
            this.activityName = activityName;
        }

        public void setStartDate(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        public void start() {
            if (listener != null) {
                listener.onStartActivity(activityName, startDate);
            }
            dialogFragment.dismiss();
        }

    }

    private class ActivityNameListener implements TextWatcher {

        private State dialogState;

        public ActivityNameListener(State dialogState) {
            this.dialogState = dialogState;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            dialogState.setActivityName(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }

    public interface OnStartActivityListener {
        void onStartActivity(String activityName, LocalDateTime startDate);
    }

}
