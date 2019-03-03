package com.example.timetrakr.view.common.dialog;

import android.content.DialogInterface;

import androidx.fragment.app.FragmentManager;

/**
 * Dialog that can be dismissed.
 */
public interface DismissableDialog {

    /**
     * Show dialog using specified fragment manager.
     *
     * @param fragmentManager fragment manager to show the dialog with
     * @param dialogName name to register the dialog with
     */
    void show(FragmentManager fragmentManager, String dialogName);

    /**
     * Specify the listener to be called, when the dialog gets dismissed by the user.
     *
     * @param onDismiss listener to call
     */
    void setOnDismiss(DialogInterface.OnDismissListener onDismiss);

}
