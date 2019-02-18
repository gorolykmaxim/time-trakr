package com.example.timetrakr.view.common;

import android.app.TimePickerDialog;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeEditTest {

    private TimeEdit timeEdit;
    private TimeEdit.TimePickerDialogFactory factory;
    private DateTimeFormatter formatter;
    private String startTimeTemplate;

    @Before
    public void setUp() throws Exception {
        formatter = DateTimeFormatter.ofPattern("HH:mm");
        startTimeTemplate = "since %s";
        timeEdit = Mockito.mock(TimeEdit.class);
        Mockito.doCallRealMethod().when(timeEdit).setFactory(Mockito.any());
        Mockito.doCallRealMethod().when(timeEdit).setOnDateChangeListener(Mockito.any());
        Mockito.doCallRealMethod().when(timeEdit).setDisplayFormat(Mockito.anyString());
        Mockito.doCallRealMethod().when(timeEdit).setFormatter(Mockito.any());
        Mockito.doCallRealMethod().when(timeEdit).update(Mockito.any());
        factory = Mockito.mock(TimeEdit.TimePickerDialogFactory.class);
        timeEdit.setFactory(factory);
        timeEdit.setDisplayFormat(startTimeTemplate);
        timeEdit.setFormatter(formatter);
    }

    @Test
    public void specifyDate() {
        LocalDateTime dateTime = LocalDateTime.now();
        timeEdit.update(dateTime);
        Mockito.verify(timeEdit).setText(String.format(startTimeTemplate, dateTime.format(formatter)));
    }

    @Test
    public void pickTimeOnTimePicker() {
        LocalDateTime dateTime = LocalDateTime.now();
        timeEdit.update(dateTime);
        TimeEdit.OnDateChangedListener listener = Mockito.mock(TimeEdit.OnDateChangedListener.class);
        timeEdit.setOnDateChangeListener(listener);
        TimePickerDialog dialog = Mockito.mock(TimePickerDialog.class);
        Mockito.when(factory.create(Mockito.eq(timeEdit.getContext()), Mockito.notNull(TimePickerDialog.OnTimeSetListener.class), Mockito.eq(dateTime.getHour()), Mockito.eq(dateTime.getMinute()), Mockito.eq(true))).thenReturn(dialog);
        ArgumentCaptor<View.OnClickListener> timeClickCaptor = ArgumentCaptor.forClass(View.OnClickListener.class);
        Mockito.verify(timeEdit).setOnClickListener(timeClickCaptor.capture());
        View.OnClickListener timeClickListener = timeClickCaptor.getValue();
        ArgumentCaptor<TimePickerDialog.OnTimeSetListener> timeSetCaptor = ArgumentCaptor.forClass(TimePickerDialog.OnTimeSetListener.class);
        // Click on the date edit view.
        timeClickListener.onClick(timeEdit);
        Mockito.verify(factory).create(Mockito.any(), timeSetCaptor.capture(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean());
        TimePickerDialog.OnTimeSetListener timeSetListener = timeSetCaptor.getValue();
        Mockito.verify(dialog).show();
        // Choose current time in time picker dialog.
        LocalDateTime expectedDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 10));
        timeSetListener.onTimeSet(null, expectedDateTime.getHour(), expectedDateTime.getMinute());
        Mockito.verify(timeEdit).setText(String.format(startTimeTemplate, expectedDateTime.format(formatter)));
        Mockito.verify(listener).onDateChange(expectedDateTime);
    }

}
