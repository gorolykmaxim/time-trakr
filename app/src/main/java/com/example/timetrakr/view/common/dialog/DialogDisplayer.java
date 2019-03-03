package com.example.timetrakr.view.common.dialog;

import androidx.fragment.app.FragmentManager;

/**
 * Controls specified dialog, so that only one such dialog is displayed on the screen
 * at a time per displayer.
 */
public class DialogDisplayer <T extends DismissableDialog> {

    private String dialogName;
    private T dialog;
    private DismissableDialogFactory<T> factory;

    /**
     * Construct a displayer.
     *
     * @param factory factory used to create a dialog instance
     * @param dialogName name to register created dialog in fragment manager with
     */
    public DialogDisplayer(DismissableDialogFactory<T> factory, String dialogName) {
        this.dialogName = dialogName;
        this.factory = factory;
    }

    /**
     * Display a dialog.
     *
     * <p>This method guarantees that only one dialog will be displayed by this displayer.</p>
     *
     * @param fragmentManager fragment manager to display dialog with
     */
    public T display(FragmentManager fragmentManager) {
        if (dialog == null) {
            dialog = factory.create();
            dialog.setOnDismiss(dlg -> dialog = null);
            dialog.show(fragmentManager, dialogName);
        }
        return dialog;
    }

}
