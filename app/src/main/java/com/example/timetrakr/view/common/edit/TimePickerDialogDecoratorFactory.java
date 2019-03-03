package com.example.timetrakr.view.common.edit;

import android.app.TimePickerDialog;
import android.content.Context;

import com.example.timetrakr.view.common.dialog.DismissableDialogFactory;

/**
 * {@link TimePickerDialogDecorator} factory.
 */
public class TimePickerDialogDecoratorFactory implements DismissableDialogFactory<TimePickerDialogDecorator> {

    private Context context;
    private TimePickerDialog.OnTimeSetListener listener;
    private int hour, minute;
    private boolean is24HourView;

    /**
     * Construct a factory.
     */
    public TimePickerDialogDecoratorFactory() {
        is24HourView = true;
    }

    /**
     * Specify context in scope of which all created time picker dialogs should be displayed.
     *
     * @param context context for created dialogs
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Specify listener to be called when user picks time on one of the created dialogs.
     *
     * @param listener listener to call
     */
    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    /**
     * Set hour to be displayed on each created time picker dialog when they get opened for the
     * first time.
     *
     * @param hour hour to initially display in dialogs
     */
    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * Set minute to be displayed on each created time picker dialog when they get opened for the
     * first time.
     *
     * @param minute minute to initially display in dialogs
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }

    /**
     * Specify whether or not to use a 24-hour display format in all created dialogs.
     *
     * @param is24HourView if set to true, 24-hours display format will be used. true by default.
     */
    public void setIs24HourView(boolean is24HourView) {
        this.is24HourView = is24HourView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimePickerDialogDecorator create() {
        if (context == null) {
            throw new IllegalStateException(String.format("%s.setContext() was not called.", getClass().getName()));
        }
        TimePickerDialog dialog = new TimePickerDialog(context, listener, hour, minute, is24HourView);
        return new TimePickerDialogDecorator(dialog);
    }

}
