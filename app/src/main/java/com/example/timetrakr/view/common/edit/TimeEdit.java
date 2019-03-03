package com.example.timetrakr.view.common.edit;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.timetrakr.R;
import com.example.timetrakr.view.common.dialog.DialogDisplayer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import androidx.annotation.Nullable;

/**
 * View, that displays specified time and opens up a time picker dialog when you click on it.
 */
public class TimeEdit extends TextView {

    private String displayFormat;
    private LocalDateTime currentDateTime;
    private DateTimeFormatter formatter;
    private DialogDisplayer<TimePickerDialogDecorator> timePickerDisplayer;
    private TimePickerDialogDecoratorFactory factory;

    /**
     * Construct a time edit view.
     *
     * @param context context in scope of which the view is displayed.
     * @param attrs attributes to initialize view with
     */
    public TimeEdit(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TimeEdit);
        currentDateTime = LocalDateTime.now();
        displayFormat = attributes.getString(R.styleable.TimeEdit_display_format);
        factory = new TimePickerDialogDecoratorFactory();
        timePickerDisplayer = new DialogDisplayer<>(factory, "time_picker_dialog");
        attributes.recycle();
    }

    /**
     * Specify factory to create time picker dialog.
     *
     * @param factory factory of time picker dialog instances
     */
    public void setFactory(TimePickerDialogDecoratorFactory factory) {
        this.factory = factory;
    }

    /**
     * Specify dialog displayer to be used to control the time picker dialog.
     *
     * @param timePickerDisplayer dialog displayer to use
     */
    public void setTimePickerDisplayer(DialogDisplayer<TimePickerDialogDecorator> timePickerDisplayer) {
        this.timePickerDisplayer = timePickerDisplayer;
    }

    /**
     * Set listener that will be called, when user will click on the text view and choose a time
     * on a time picker dialog.
     *
     * @param onDateChangeListener listener to call on a date change
     */
    public void setOnDateChangeListener(OnDateChangedListener onDateChangeListener) {
        setOnClickListener(v -> {
            TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
                LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hourOfDay, minute));
                update(dateTime);
                onDateChangeListener.onDateChange(dateTime);
            };
            factory.setContext(getContext());
            factory.setListener(listener);
            factory.setHour(currentDateTime.getHour());
            factory.setMinute(currentDateTime.getMinute());
            // Dialog presented in this displayer does not need a fragment manager to be displayed.
            timePickerDisplayer.display(null);
        });
    }

    /**
     * Specify string template, used to display time on the text view.
     *
     * @param displayFormat time display template
     */
    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
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
     * <p>Calling this method will not trigger a {@link OnDateChangedListener}.</p>
     *
     * @param dateTime date-time to display
     */
    public void update(LocalDateTime dateTime) {
        currentDateTime = dateTime;
        setText(String.format(displayFormat, dateTime.format(formatter)));
    }

    /**
     * Listener called when user selects time in the opened time picker dialog.
     */
    public interface OnDateChangedListener {

        /**
         * Callback called on date-time selected.
         *
         * @param dateTime selected date-time.
         */
        void onDateChange(LocalDateTime dateTime);
    }

}
