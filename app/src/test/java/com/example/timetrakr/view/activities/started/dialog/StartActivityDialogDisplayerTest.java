package com.example.timetrakr.view.activities.started.dialog;

import android.content.DialogInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import androidx.fragment.app.FragmentManager;

public class StartActivityDialogDisplayerTest {

    private StartActivityDialogDisplayer displayer;
    private StartActivityDialogFragment dialogFragment;
    private StartActivityDialogFragment.OnStartActivityListener onStartActivityListener;
    private String dialogName, activityName;
    private LocalDateTime startDate;
    private FragmentManager fragmentManager;
    private ArgumentCaptor<StartActivityDialogFragment.OnStartActivityListener> startActivityCaptor;
    private ArgumentCaptor<DialogInterface.OnCancelListener> cancelCaptor;

    @Before
    public void setUp() throws Exception {
        dialogName = "start_activity_dialog";
        activityName = "I'm out of ideas and i got to go to work";
        startDate = LocalDateTime.now();
        dialogFragment = Mockito.mock(StartActivityDialogFragment.class);
        StartActivityDialogDisplayerFactory factory = Mockito.mock(StartActivityDialogDisplayerFactory.class);
        Mockito.when(factory.create()).thenReturn(dialogFragment);
        onStartActivityListener = Mockito.mock(StartActivityDialogFragment.OnStartActivityListener.class);
        displayer = new StartActivityDialogDisplayer(factory, dialogName);
        displayer.setOnStartActivityListener(onStartActivityListener);
        fragmentManager = Mockito.mock(FragmentManager.class);
        startActivityCaptor = ArgumentCaptor.forClass(StartActivityDialogFragment.OnStartActivityListener.class);
        cancelCaptor = ArgumentCaptor.forClass(DialogInterface.OnCancelListener.class);
    }

    @Test
    public void displayWithNoActivityAndStart() {
        // User tries to open the same dialog twice.
        displayer.display(fragmentManager);
        displayer.display(fragmentManager);
        // Only one actual dialog should be shown though.
        Mockito.verify(dialogFragment, Mockito.times(1)).show(fragmentManager, dialogName);
        Mockito.verify(dialogFragment).setOntStartActivityListener(startActivityCaptor.capture());
        StartActivityDialogFragment.OnStartActivityListener startActivity = startActivityCaptor.getValue();
        // After starting activity, the dialog should close and we should be able to show it again.
        startActivity.onStartActivity(activityName, startDate);
        Mockito.verify(onStartActivityListener).onStartActivity(activityName, startDate);
        Mockito.reset(dialogFragment);
        displayer.display(fragmentManager);
        Mockito.verify(dialogFragment, Mockito.times(1)).show(fragmentManager, dialogName);
    }

    @Test
    public void displayWithActivityAndCancel() {
        displayer.display(activityName, fragmentManager);
        Mockito.verify(dialogFragment, Mockito.times(1)).show(fragmentManager, dialogName);
        Mockito.verify(dialogFragment).setActivityName(activityName);
        Mockito.verify(dialogFragment).setOnCancelListener(cancelCaptor.capture());
        // After cancelling the activity start, the dialog should close and we should be able to show it again.
        DialogInterface.OnCancelListener cancel = cancelCaptor.getValue();
        cancel.onCancel(Mockito.mock(DialogInterface.class));
        Mockito.reset(dialogFragment);
        displayer.display(activityName, fragmentManager);
        Mockito.verify(dialogFragment, Mockito.times(1)).show(fragmentManager, dialogName);
    }
}
