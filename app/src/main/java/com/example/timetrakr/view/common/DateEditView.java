package com.example.timetrakr.view.common;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Facade for a text view, that displays specified time and opens up a time picker dialog
 * when you click on it.
 */
public class DateEditView {

    private TextView textView;
    private String startTimeTemplate;
    private DateTimeFormatter formatter;
    private LocalDateTime currentDateTime;
    private OnDateChangeListener onDateChangeListener;
    private TimePickerDialogFactory factory;

    /**
     * Construct the view.
     */
    public DateEditView() {
        factory = new TimePickerDialogFactory();
        currentDateTime = LocalDateTime.now();
    }

    /**
     * Specify factory to create time picker dialog.
     *
     * @param factory factory of time picker dialog instances
     */
    public void setFactory(TimePickerDialogFactory factory) {
        this.factory = factory;
    }

    /**
     * Set listener that will be called, when user will click on the text view and choose a time
     * on a time picker dialog.
     *
     * @param onDateChangeListener listener to call on a date change
     */
    public void setOnDateChangeListener(OnDateChangeListener onDateChangeListener) {
        this.onDateChangeListener = onDateChangeListener;
    }

    /**
     * Specify text view to display time in and listen to click events on.
     *
     * @param textView text view to use
     */
    public void setTextView(TextView textView) {
        this.textView = textView;
        textView.setOnClickListener(v -> {
            TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
                LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hourOfDay, minute));
                update(dateTime);
                if (onDateChangeListener != null) {
                    onDateChangeListener.onDateChange(dateTime);
                }
            };
            TimePickerDialog dialog = factory.create(textView.getContext(), listener, currentDateTime.getHour(), currentDateTime.getMinute(), true);
            dialog.show();
        });
    }

    /**
     * Specify string template, used to display time on the text view.
     *
     * @param startTimeTemplate time display template
     */
    public void setStartTimeTemplate(String startTimeTemplate) {
        this.startTimeTemplate = startTimeTemplate;
    }

    /**
     * Specify formatter, used to format displayed date-time.
     *
     * @param formatter formatter to format the date-time
     */
    public void setFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * Display specified date-time in the text view.
     *
     * <p>Calling this method will not trigger a {@link OnDateChangeListener}.</p>
     *
     * @param dateTime date-time to display
     */
    public void update(LocalDateTime dateTime) {
        currentDateTime = dateTime;
        textView.setText(String.format(startTimeTemplate, dateTime.format(formatter)));
    }

    /**
     * Factory used to create time picker dialog instances by default.
     */
    public static class TimePickerDialogFactory {

        /**
         * Create a time picker dialog instance.
         *
         * @param context context in scope of which dialog will be displayed
         * @param listener listener called when user selects time in the dialog
         * @param hour initial hour to be displayed in the dialog
         * @param minute initial minute to be displayed in the dialog
         * @param is24HourView if set to true, dialog will display time in 24 hours format
         * @return time picker dialog
         */
        public TimePickerDialog create(Context context, TimePickerDialog.OnTimeSetListener listener, int hour, int minute, boolean is24HourView) {
            return new TimePickerDialog(context, listener, hour, minute, is24HourView);
        }
    }

    /**
     * Listener called when user selects time in the opened time picker dialog.
     */
    public interface OnDateChangeListener {

        /**
         * Callback called on date-time selected.
         *
         * @param dateTime selected date-time.
         */
        void onDateChange(LocalDateTime dateTime);
    }

}
