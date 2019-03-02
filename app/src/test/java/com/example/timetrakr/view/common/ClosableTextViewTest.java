package com.example.timetrakr.view.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.timetrakr.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ClosableTextViewTest {

    private ClosableTextView closableTextView;
    private TextView textView;
    private ImageButton closeButton;
    private View.OnClickListener onClick;

    @Before
    public void setUp() throws Exception {
        textView = Mockito.mock(TextView.class);
        closeButton = Mockito.mock(ImageButton.class);
        closableTextView = Mockito.mock(ClosableTextView.class);
        Mockito.doCallRealMethod().when(closableTextView).initialize(Mockito.any(), Mockito.any());
        Mockito.doCallRealMethod().when(closableTextView).setOnCloseListener(Mockito.any());
        Mockito.doCallRealMethod().when(closableTextView).setText(Mockito.anyString());
        Mockito.doCallRealMethod().when(closableTextView).show();
        Mockito.doCallRealMethod().when(closableTextView).hide();
        Mockito.when(closableTextView.findViewById(R.id.text)).thenReturn(textView);
        Mockito.when(closableTextView.findViewById(R.id.close_button)).thenReturn(closeButton);
        Context context = Mockito.mock(Context.class);
        AttributeSet attrs = Mockito.mock(AttributeSet.class);
        ArgumentCaptor<View.OnClickListener> onClickCaptor = ArgumentCaptor.forClass(View.OnClickListener.class);
        // Create closable text view.
        closableTextView.initialize(context, attrs);
        Mockito.verify(closeButton).setOnClickListener(onClickCaptor.capture());
        onClick = onClickCaptor.getValue();
    }

    @Test
    public void closeClick() {
        // Specify listener to be called when closable text view closes.
        ClosableTextView.OnCloseListener onCloseListener = Mockito.mock(ClosableTextView.OnCloseListener.class);
        closableTextView.setOnCloseListener(onCloseListener);
        // Close the text view.
        onClick.onClick(closeButton);
        Mockito.verify(closableTextView).setVisibility(View.INVISIBLE);
        Mockito.verify(onCloseListener).onClose();
    }

    @Test
    public void setText() {
        String expectedText = "Text to display in the text view";
        closableTextView.setText(expectedText);
        Mockito.verify(textView).setText(expectedText);
    }

    @Test
    public void show() {
        closableTextView.show();
        Mockito.verify(closableTextView).setVisibility(View.VISIBLE);
    }

    @Test
    public void hide() {
        closableTextView.hide();
        Mockito.verify(closableTextView).setVisibility(View.INVISIBLE);
    }

}
