package com.example.timetrakr.view.common.edit;

import android.app.TimePickerDialog;
import android.view.View;

import com.example.timetrakr.view.common.dialog.DialogDisplayer;

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
    private DialogDisplayer<TimePickerDialogDecorator> dialogDisplayer;
    private TimePickerDialogDecoratorFactory factory;
    private DateTimeFormatter formatter;
    private String startTimeTemplate;

    @Before
    public void setUp() throws Exception {
        formatter = DateTimeFormatter.ofPattern("HH:mm");
        startTimeTemplate = "since %s";
        timeEdit = Mockito.mock(TimeEdit.class);
        factory = Mockito.mock(TimePickerDialogDecoratorFactory.class);
        dialogDisplayer = Mockito.mock(DialogDisplayer.class);
        Mockito.doCallRealMethod().when(timeEdit).setFactory(Mockito.any());
        Mockito.doCallRealMethod().when(timeEdit).setTimePickerDisplayer(Mockito.any());
        Mockito.doCallRealMethod().when(timeEdit).setOnDateChangeListener(Mockito.any());
        Mockito.doCallRealMethod().when(timeEdit).setDisplayFormat(Mockito.anyString());
        Mockito.doCallRealMethod().when(timeEdit).setFormatter(Mockito.any());
        Mockito.doCallRealMethod().when(timeEdit).update(Mockito.any());
        timeEdit.setFactory(factory);
        timeEdit.setTimePickerDisplayer(dialogDisplayer);
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
        ArgumentCaptor<View.OnClickListener> timeClickCaptor = ArgumentCaptor.forClass(View.OnClickListener.class);
        Mockito.verify(timeEdit).setOnClickListener(timeClickCaptor.capture());
        View.OnClickListener timeClickListener = timeClickCaptor.getValue();
        ArgumentCaptor<TimePickerDialog.OnTimeSetListener> timeSetCaptor = ArgumentCaptor.forClass(TimePickerDialog.OnTimeSetListener.class);
        // Click on the date edit view.
        timeClickListener.onClick(timeEdit);
        Mockito.verify(factory).setContext(timeEdit.getContext());
        Mockito.verify(factory).setListener(timeSetCaptor.capture());
        Mockito.verify(factory).setHour(Mockito.anyInt());
        Mockito.verify(factory).setMinute(Mockito.anyInt());
        TimePickerDialog.OnTimeSetListener timeSetListener = timeSetCaptor.getValue();
        Mockito.verify(dialogDisplayer).display(null);
        // Choose current time in time picker dialog.
        LocalDateTime expectedDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 10));
        timeSetListener.onTimeSet(null, expectedDateTime.getHour(), expectedDateTime.getMinute());
        Mockito.verify(timeEdit).setText(String.format(startTimeTemplate, expectedDateTime.format(formatter)));
        Mockito.verify(listener).onDateChange(expectedDateTime);
    }

}
