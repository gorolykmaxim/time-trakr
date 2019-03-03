package com.example.timetrakr.view.activities.started.dialog;

import android.content.DialogInterface;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.timetrakr.R;
import com.example.timetrakr.view.common.TimeEdit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import androidx.fragment.app.DialogFragment;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DialogFragment.class})
public class StartActivityDialogFragmentTest {

    private DateTimeFormatter formatter;
    private StartActivityDialogFactory factory;
    private StartActivityDialogFragment dialogFragment;
    private StartActivityDialogFragment.OnStartActivityListener listener;
    private DialogInterface.OnDismissListener onDismissListener;
    private LayoutInflater inflater;
    private ViewGroup container, viewGroup;
    private EditText activityNameEditText;
    private TimeEdit timeEdit;
    private Button startButton;
    private ArgumentCaptor<View.OnClickListener> onStartClickCaptor;
    private ArgumentCaptor<TextView.OnEditorActionListener> onEditorActionCaptor;
    private ArgumentCaptor<TextWatcher> textWatcherCaptor;
    private ArgumentCaptor<LocalDateTime> startDateCaptor;
    private ArgumentCaptor<TimeEdit.OnDateChangedListener> onDateChangeCaptor;

    @Before
    public void setUp() throws Exception {
        formatter = DateTimeFormatter.ofPattern("HH:mm");
        factory = new StartActivityDialogFactory(formatter);
        listener = Mockito.mock(StartActivityDialogFragment.OnStartActivityListener.class);
        factory.setOnStartActivityListener(listener);
        dialogFragment = factory.create();
        onDismissListener = Mockito.mock(DialogInterface.OnDismissListener.class);
        dialogFragment.setOnDismiss(onDismissListener);
        // Initialize android views.
        inflater = Mockito.mock(LayoutInflater.class);
        container = Mockito.mock(ViewGroup.class);
        viewGroup = Mockito.mock(ViewGroup.class);
        activityNameEditText = Mockito.mock(EditText.class);
        Mockito.when(viewGroup.findViewById(R.id.start_activity_dialog_activity_name)).thenReturn(activityNameEditText);
        timeEdit = Mockito.mock(TimeEdit.class);
        Mockito.when(viewGroup.findViewById(R.id.start_activity_dialog_time)).thenReturn(timeEdit);
        startButton = Mockito.mock(Button.class);
        Mockito.when(viewGroup.findViewById(R.id.start_activity_dialog_start_button)).thenReturn(startButton);
        Mockito.when(inflater.inflate(R.layout.start_activity_dialog, container, false)).thenReturn(viewGroup);
        PowerMockito.stub(PowerMockito.method(DialogFragment.class, "dismiss")).toReturn(null);
        // Create all captors.
        onStartClickCaptor = ArgumentCaptor.forClass(View.OnClickListener.class);
        onEditorActionCaptor = ArgumentCaptor.forClass(TextView.OnEditorActionListener.class);
        textWatcherCaptor = ArgumentCaptor.forClass(TextWatcher.class);
        startDateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        onDateChangeCaptor = ArgumentCaptor.forClass(TimeEdit.OnDateChangedListener.class);
    }

    @Test
    public void changesPositionOnTheScreenAppropriatelyWhenOnScreenKeyboardAppears() {
        StartActivityDialogFragment dialogFragmentSpy = Mockito.spy(dialogFragment);
        dialogFragmentSpy.onCreate(null);
        Mockito.verify(dialogFragmentSpy).setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Test
    public void prolongExistingActivity() {
        String name = "Resting finally";
        dialogFragment.setActivityName(name);
        View view = dialogFragment.onCreateView(inflater, container, null);
        Assert.assertEquals(viewGroup, view);
        Mockito.verify(activityNameEditText).setText(name);
        viewStateInitialization();
        // Click on save immediately since all fields are automatically set.
        View.OnClickListener onStartClick = onStartClickCaptor.getValue();
        onStartClick.onClick(startButton);
        Mockito.verify(listener).onStartActivity(name, startDateCaptor.getValue());
    }

    @Test
    public void createNewActivityEnterNameAndPressEnter() {
        String name = "maybe resting in an hour";
        View view = dialogFragment.onCreateView(inflater, container, null);
        Assert.assertEquals(viewGroup, view);
        viewStateInitialization();
        // Enter new activity name.
        TextWatcher textWatcher = textWatcherCaptor.getValue();
        textWatcher.onTextChanged(name, 0, 0, 0);
        // Press enter.
        TextView.OnEditorActionListener editorActionListener = onEditorActionCaptor.getValue();
        // Actually, let's send a noise event to the listener just to make sure, onStartActivity()
        // would not get called for it as well.
        editorActionListener.onEditorAction(activityNameEditText, 0, Mockito.mock(KeyEvent.class));
        editorActionListener.onEditorAction(activityNameEditText, 0, null);
        Mockito.verify(listener, Mockito.times(1)).onStartActivity(name, startDateCaptor.getValue());
        Mockito.verify(onDismissListener, Mockito.times(1)).onDismiss(dialogFragment.getDialog());
    }

    @Test
    public void createNewActivityEnterNameSetTimeAndPressStart() {
        String name = "finally rest (this time for sure)";
        LocalDateTime expectedStartDate = LocalDateTime.now();
        View view = dialogFragment.onCreateView(inflater, container, null);
        Assert.assertEquals(viewGroup, view);
        viewStateInitialization();
        // Enter new activity name.
        TextWatcher textWatcher = textWatcherCaptor.getValue();
        textWatcher.onTextChanged(name, 0, 0, 0);
        // Set start time.
        TimeEdit.OnDateChangedListener onDateChange = onDateChangeCaptor.getValue();
        onDateChange.onDateChange(expectedStartDate);
        // Press start.
        View.OnClickListener onStartClick = onStartClickCaptor.getValue();
        onStartClick.onClick(startButton);
        Mockito.verify(listener).onStartActivity(name, expectedStartDate);
        Mockito.verify(onDismissListener, Mockito.times(1)).onDismiss(dialogFragment.getDialog());
    }

    @Test
    public void createNewActivityWithEmptyName() {
        View view = dialogFragment.onCreateView(inflater, container, null);
        Assert.assertEquals(viewGroup, view);
        viewStateInitialization();
        // Just press start without doing anything.
        View.OnClickListener onStartClick = onStartClickCaptor.getValue();
        onStartClick.onClick(startButton);
        Mockito.verify(listener).onStartActivity("", startDateCaptor.getValue());
        Mockito.verify(onDismissListener, Mockito.times(1)).onDismiss(dialogFragment.getDialog());
    }

    @Test
    public void cancelDialog() {
        DialogInterface dialog = Mockito.mock(DialogInterface.class);
        DialogInterface.OnCancelListener onCancelListener = Mockito.mock(DialogInterface.OnCancelListener.class);
        dialogFragment.setOnCancelListener(onCancelListener);
        dialogFragment.onCancel(dialog);
        Mockito.verify(onCancelListener).onCancel(dialog);
        Mockito.verify(onDismissListener, Mockito.times(1)).onDismiss(dialog);
    }

    private void viewStateInitialization() {
        Mockito.verify(activityNameEditText).setOnEditorActionListener(onEditorActionCaptor.capture());
        Mockito.verify(activityNameEditText).addTextChangedListener(textWatcherCaptor.capture());
        Mockito.verify(timeEdit).setFormatter(formatter);
        Mockito.verify(timeEdit).update(startDateCaptor.capture());
        Mockito.verify(timeEdit).setOnDateChangeListener(onDateChangeCaptor.capture());
        LocalDateTime dateTime = startDateCaptor.getValue();
        Assert.assertTrue(dateTime.compareTo(LocalDateTime.now()) <= 0);
        Mockito.verify(startButton).setOnClickListener(onStartClickCaptor.capture());
    }

}
