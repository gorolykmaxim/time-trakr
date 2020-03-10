package com.example.timetrakr.view.activities.started.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.timetrakr.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.time.LocalDateTime;

/**
 * Dialog fragment, that allows user to start new activities.
 */
public class StartActivityDialogFragment extends BottomSheetDialogFragment {
    private String defaultActivityName;
    private OnStartActivityListener onStartActivityListener;
    private EditText activityNameEditText;
    private ActivityStartTimeEdit timeEdit;
    private Button startButton;

    /**
     * Specify listener to be called when user saves newly created activity.
     *
     * @param listener listener to call
     */
    public void setOntStartActivityListener(OnStartActivityListener listener) {
        onStartActivityListener = listener;
    }

    /**
     * Specify an activity name to be displayed when user opens up a dialog.
     *
     * @param defaultActivityName activity name to display
     */
    public void setDefaultActivityName(String defaultActivityName) {
        this.defaultActivityName = defaultActivityName;
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
    public void onDismiss(@NonNull DialogInterface dialog) {
        activityNameEditText.setText("");
        timeEdit.reset();
        super.onDismiss(dialog);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.start_activity_dialog, container, false);
        activityNameEditText = viewGroup.findViewById(R.id.start_activity_dialog_activity_name);
        timeEdit = viewGroup.findViewById(R.id.start_activity_dialog_time);
        startButton = viewGroup.findViewById(R.id.start_activity_dialog_start_button);
        return viewGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
        if (defaultActivityName != null) {
            activityNameEditText.setText(defaultActivityName);
        }
        activityNameEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (event == null) {
                finishActivityStart();
                return true;
            } else {
                return false;
            }
        });
        startButton.setOnClickListener(v -> finishActivityStart());
    }

    private void finishActivityStart() {
        onStartActivityListener.onStartActivity(activityNameEditText.getText().toString(), timeEdit.getDateTime());
        dismiss();
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
