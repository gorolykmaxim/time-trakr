package com.example.timetrakr.view.common.edit;

import android.app.TimePickerDialog;
import android.content.DialogInterface;

import com.example.timetrakr.view.common.dialog.DismissableDialog;

import androidx.fragment.app.FragmentManager;

/**
 * Decorator to adapt {@link TimePickerDialog} interface to {@link DismissableDialog} interface contract.
 */
public class TimePickerDialogDecorator implements DismissableDialog {

    private TimePickerDialog timePickerDialog;

    /**
     * Construct a decorator.
     *
     * @param timePickerDialog actual time picker dialog to decorate
     */
    public TimePickerDialogDecorator(TimePickerDialog timePickerDialog) {
        this.timePickerDialog = timePickerDialog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(FragmentManager fragmentManager, String dialogName) {
        timePickerDialog.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnDismiss(DialogInterface.OnDismissListener onDismiss) {
        timePickerDialog.setOnDismissListener(onDismiss);
    }

}
