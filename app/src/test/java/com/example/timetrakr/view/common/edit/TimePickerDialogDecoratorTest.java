package com.example.timetrakr.view.common.edit;

import android.app.TimePickerDialog;
import android.content.DialogInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TimePickerDialogDecoratorTest {

    private TimePickerDialog dialog;
    private TimePickerDialogDecorator decorator;

    @Before
    public void setUp() throws Exception {
        dialog = Mockito.mock(TimePickerDialog.class);
        decorator = new TimePickerDialogDecorator(dialog);
    }

    @Test
    public void setOnDismiss() {
        DialogInterface.OnDismissListener onDismissListener = Mockito.mock(DialogInterface.OnDismissListener.class);
        decorator.setOnDismiss(onDismissListener);
        Mockito.verify(dialog).setOnDismissListener(onDismissListener);
    }

    @Test
    public void show() {
        decorator.show(null, null);
        Mockito.verify(dialog).show();
    }

}
