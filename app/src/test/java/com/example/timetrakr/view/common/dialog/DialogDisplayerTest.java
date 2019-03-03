package com.example.timetrakr.view.common.dialog;

import android.content.DialogInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import androidx.fragment.app.FragmentManager;

public class DialogDisplayerTest {

    private DismissableDialog dialog;
    private DismissableDialogFactory<DismissableDialog> factory;
    private String dialogName;
    private FragmentManager fragmentManager;
    private DialogDisplayer<DismissableDialog> displayer;

    @Before
    public void setUp() throws Exception {
        dialog = Mockito.mock(DismissableDialog.class);
        factory = Mockito.mock(DismissableDialogFactory.class);
        Mockito.when(factory.create()).thenReturn(dialog);
        dialogName = "dialog";
        fragmentManager = Mockito.mock(FragmentManager.class);
        displayer = new DialogDisplayer<>(factory, dialogName);
    }

    @Test
    public void test() {
        ArgumentCaptor<DialogInterface.OnDismissListener> onDismissCaptor = ArgumentCaptor.forClass(DialogInterface.OnDismissListener.class);
        // User tries to open the same dialog twice.
        displayer.display(fragmentManager);
        displayer.display(fragmentManager);
        // Only one actual dialog should be shown though.
        Mockito.verify(dialog).setOnDismiss(onDismissCaptor.capture());
        Mockito.verify(dialog, Mockito.times(1)).show(fragmentManager, dialogName);
        // After closing the dialog we should be able to show it again.
        DialogInterface.OnDismissListener onDismissListener = onDismissCaptor.getValue();
        onDismissListener.onDismiss(Mockito.mock(DialogInterface.class));
        Mockito.reset(dialog);
        displayer.display(fragmentManager);
        Mockito.verify(dialog, Mockito.times(1)).show(fragmentManager, dialogName);
    }

}
