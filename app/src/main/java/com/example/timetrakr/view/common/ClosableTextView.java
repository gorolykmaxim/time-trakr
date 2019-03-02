package com.example.timetrakr.view.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.timetrakr.R;

/**
 * A simple text view that has a close icon next to it and can be closed with it.
 */
public class ClosableTextView extends RelativeLayout {

    private TextView textView;
    private OnCloseListener onCloseListener;

    /**
     * Construct a text view.
     *
     * @param context context in scope of which the text view will be displayed
     * @param attrs attributes to initialize text view with
     */
    public ClosableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.closable_text_view, this);
        initialize(context, attrs);
    }

    /**
     * Initialize the view.
     *
     * <p>Used only for testing purposes, since it is really hard to test constructors
     * of android view.</p>
     *
     * @param context context in scope of which the text view will be displayed
     * @param attrs attributes to initialize text view with
     */
    void initialize(Context context, AttributeSet attrs) {
        textView = findViewById(R.id.text);
        ImageButton closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> {
            hide();
            if (onCloseListener != null) {
                onCloseListener.onClose();
            }
        });
    }

    /**
     * Specify listener that will be called when the text view will get closed by the user.
     *
     * @param onCloseListener listener to call on closure
     */
    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    /**
     * Display specified text in the text view.
     *
     * @param text text to display
     */
    public void setText(String text) {
        textView.setText(text);
    }

    /**
     * Make text view visible.
     */
    public void show() {
        setVisibility(VISIBLE);
    }

    /**
     * Hide text view.
     *
     * <p>This will not trigger {@link OnCloseListener} call.</p>
     */
    public void hide() {
        setVisibility(INVISIBLE);
    }

    /**
     * Listener, called when a {@link ClosableTextView} gets closed by the user.
     */
    public interface OnCloseListener {

        /**
         * Called on the text view closure.
         */
        void onClose();
    }

}
