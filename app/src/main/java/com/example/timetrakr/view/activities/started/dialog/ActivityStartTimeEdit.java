package com.example.timetrakr.view.activities.started.dialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.timetrakr.R;
import com.gorolykmaxim.android.commons.dialog.DialogServant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Time edit field for specifying activity start date time.
 */
public class ActivityStartTimeEdit extends AppCompatTextView implements TimePickerDialog.OnTimeSetListener {
    private String displayFormat;
    private DateTimeFormatter formatter;
    private LocalDateTime currentDateTime;
    private DialogServant dialogServant;
    private TimePickerDialog dialog;

    /**
     * {@inheritDoc}
     */
    public ActivityStartTimeEdit(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ActivityStartTimeEdit);
        displayFormat = attributes.getString(R.styleable.ActivityStartTimeEdit_display_format);
        formatter = DateTimeFormatter.ofPattern(attributes.getString(R.styleable.ActivityStartTimeEdit_date_time_format));
        attributes.recycle();
        reset();
        dialogServant = new DialogServant();
        dialog = new TimePickerDialog(context, this, currentDateTime.getHour(), currentDateTime.getMinute(), true);
        setOnClickListener(view -> dialogServant.showIfNotShown(dialog));
    }

    /**
     * Reset current date time value of this time edit view.
     */
    public void reset() {
        setDateTime(LocalDateTime.now());
    }

    /**
     * Specify current date time of this time edit view.
     *
     * @param currentDateTime current date time
     */
    public void setDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
        setText(String.format(displayFormat, currentDateTime.format(formatter)));
    }

    /**
     * Get specified activity start date time.
     *
     * @return activity start date time
     */
    public LocalDateTime getDateTime() {
        return currentDateTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        setDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(i, i1)));
    }
}
