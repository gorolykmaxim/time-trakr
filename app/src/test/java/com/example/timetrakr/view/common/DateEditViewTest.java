package com.example.timetrakr.view.common;

import android.app.TimePickerDialog;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateEditViewTest {

    private DateEditView dateEditView;
    private DateEditView.TimePickerDialogFactory factory;
    private DateTimeFormatter formatter;
    private TextView textView;
    private String startTimeTemplate;

    @Before
    public void setUp() throws Exception {
        formatter = DateTimeFormatter.ofPattern("HH:mm");
        startTimeTemplate = "since %s";
        textView = Mockito.mock(TextView.class);
        dateEditView = new DateEditView();
        factory = Mockito.mock(DateEditView.TimePickerDialogFactory.class);
        dateEditView.setFactory(factory);
        dateEditView.setStartTimeTemplate(startTimeTemplate);
        dateEditView.setFormatter(formatter);
        dateEditView.setTextView(textView);
    }

    @Test
    public void specifyDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        dateEditView.update(dateTime);
        Mockito.verify(textView).setText(String.format(startTimeTemplate, dateTime.format(formatter)));
    }

    @Test
    public void pickTimeOnTimePicker() {
        LocalDateTime dateTime = LocalDateTime.now();
        dateEditView.update(dateTime);
        DateEditView.OnDateChangeListener listener = Mockito.mock(DateEditView.OnDateChangeListener.class);
        dateEditView.setOnDateChangeListener(listener);
        TimePickerDialog dialog = Mockito.mock(TimePickerDialog.class);
        Mockito.when(factory.create(Mockito.eq(textView.getContext()), Mockito.notNull(TimePickerDialog.OnTimeSetListener.class), Mockito.eq(dateTime.getHour()), Mockito.eq(dateTime.getMinute()), Mockito.eq(true))).thenReturn(dialog);
        ArgumentCaptor<View.OnClickListener> timeClickCaptor = ArgumentCaptor.forClass(View.OnClickListener.class);
        Mockito.reset(textView);
        dateEditView.setTextView(textView);
        Mockito.verify(textView).setOnClickListener(timeClickCaptor.capture());
        View.OnClickListener timeClickListener = timeClickCaptor.getValue();
        ArgumentCaptor<TimePickerDialog.OnTimeSetListener> timeSetCaptor = ArgumentCaptor.forClass(TimePickerDialog.OnTimeSetListener.class);
        // Click on the date edit view.
        timeClickListener.onClick(textView);
        Mockito.verify(factory).create(Mockito.any(), timeSetCaptor.capture(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean());
        TimePickerDialog.OnTimeSetListener timeSetListener = timeSetCaptor.getValue();
        Mockito.verify(dialog).show();
        // Choose current time in time picker dialog.
        LocalDateTime expectedDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 10));
        timeSetListener.onTimeSet(null, expectedDateTime.getHour(), expectedDateTime.getMinute());
        Mockito.verify(textView).setText(String.format(startTimeTemplate, expectedDateTime.format(formatter)));
        Mockito.verify(listener).onDateChange(expectedDateTime);
    }
}
