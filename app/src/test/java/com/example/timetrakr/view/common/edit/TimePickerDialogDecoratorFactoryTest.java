package com.example.timetrakr.view.common.edit;

import android.app.TimePickerDialog;
import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TimePickerDialogDecoratorFactoryTest {

    private TimePickerDialogDecoratorFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new TimePickerDialogDecoratorFactory();
    }

    @Test
    public void test() {
        Context context = Mockito.mock(Context.class);
        TimePickerDialog.OnTimeSetListener listener = Mockito.mock(TimePickerDialog.OnTimeSetListener.class);
        factory.setContext(context);
        factory.setListener(listener);
        factory.setHour(1);
        factory.setMinute(1);
        factory.setIs24HourView(true);
        Assert.assertNotNull(factory.create());
    }

    @Test(expected = IllegalStateException.class)
    public void contextNotSet() {
        factory.create();
    }

}
