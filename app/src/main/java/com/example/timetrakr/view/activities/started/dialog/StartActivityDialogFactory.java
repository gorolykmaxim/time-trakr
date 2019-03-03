package com.example.timetrakr.view.activities.started.dialog;

import com.example.timetrakr.view.common.dialog.DismissableDialogFactory;

import java.time.format.DateTimeFormatter;

/**
 * Factory of {@link StartActivityDialogFragment} instances.
 */
public class StartActivityDialogFactory implements DismissableDialogFactory<StartActivityDialogFragment> {

    private DateTimeFormatter formatter;
    private StartActivityDialogFragment.OnStartActivityListener onStartActivityListener;

    /**
     * Construct a factory.
     *
     * @param formatter formatter used to display a start date-time in the create dialogs
     */
    public StartActivityDialogFactory(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * Specify a listener to be called when user saves new activity using one of created dialogs.
     *
     * @param onStartActivityListener listener to be called on new activity creation
     */
    public void setOnStartActivityListener(StartActivityDialogFragment.OnStartActivityListener onStartActivityListener) {
        this.onStartActivityListener = onStartActivityListener;
    }

    /**
     * Create activity start dialog.
     *
     * @return created activity start dialog
     */
    @Override
    public StartActivityDialogFragment create() {
        StartActivityDialogFragment dialogFragment =  StartActivityDialogFragment.create(formatter);
        dialogFragment.setOntStartActivityListener(onStartActivityListener);
        return dialogFragment;
    }

}
