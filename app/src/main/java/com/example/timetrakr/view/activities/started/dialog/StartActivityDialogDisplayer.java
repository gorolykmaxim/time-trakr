package com.example.timetrakr.view.activities.started.dialog;

import androidx.fragment.app.FragmentManager;

/**
 * Controls the start activity dialog, so that only one such dialog is displayed on the screen
 * at a time per displayer.
 */
public class StartActivityDialogDisplayer {

    private String dialogName;
    private StartActivityDialogFragment dialogFragment;
    private StartActivityDialogDisplayerFactory factory;
    private StartActivityDialogFragment.OnStartActivityListener onStartActivityListener;

    /**
     * Construct a displayer.
     *
     * @param factory factory used to create start activity dialog instance
     * @param dialogName name to register created dialog in fragment manager with
     */
    public StartActivityDialogDisplayer(StartActivityDialogDisplayerFactory factory, String dialogName) {
        this.factory = factory;
        this.dialogName = dialogName;
    }

    /**
     * Specify a listener to be called when user saves new activity.
     *
     * @param onStartActivityListener listener to be called on new activity creation
     */
    public void setOnStartActivityListener(StartActivityDialogFragment.OnStartActivityListener onStartActivityListener) {
        this.onStartActivityListener = onStartActivityListener;
    }

    /**
     * Display a dialog.
     *
     * <p>This method guarantees that only one dialog will be displayed by this displayer.</p>
     *
     * @param fragmentManager fragment manager to display dialog with
     */
    public void display(FragmentManager fragmentManager) {
        display(null, fragmentManager);
    }

    /**
     * Display a dialog.
     *
     * <p>This method guarantees that only one dialog will be displayed by this displayer.</p>
     *
     * @param activityName name of the activity to display in the dialog
     * @param fragmentManager fragment manager to display dialog with
     */
    public void display(String activityName, FragmentManager fragmentManager) {
        if (dialogFragment == null) {
            dialogFragment = factory.create();
            if (activityName != null) {
                dialogFragment.setActivityName(activityName);
            }
            dialogFragment.setOntStartActivityListener((name, startDate) -> {
                dialogFragment = null;
                if (onStartActivityListener != null) {
                    onStartActivityListener.onStartActivity(name, startDate);
                }
            });
            dialogFragment.setOnCancelListener(dialog -> dialogFragment = null);
            dialogFragment.show(fragmentManager, dialogName);
        }
    }
    
}
