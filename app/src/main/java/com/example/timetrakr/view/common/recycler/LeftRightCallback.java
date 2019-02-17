package com.example.timetrakr.view.common.recycler;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link ItemTouchHelper.Callback} that reacts to left and right swipes.
 *
 * <p>You can specify background color of a drawable, that will be drawn beneath the recycler view
 * item being swiped, drawable, that will be drawn on top of that background and a listener,
 * that will be called when a swipe occurs. You can specify all those things separately for a left
 * and a right swipe.</p>
 *
 */
public class LeftRightCallback extends ItemTouchHelper.SimpleCallback {

    private Drawable leftSwipeIcon, rightSwipeIcon;
    private ColorDrawable leftSwipeBackground, rightSwipeBackground;
    private OnSwipeListener leftSwipeListener, rightSwipeListener;
    private int padding;

    /**
     * Creates a Callback for the given drag and swipe allowance. These values serve as
     * defaults
     * and if you want to customize behavior per ViewHolder, you can override
     * {@link #getSwipeDirs(RecyclerView, ViewHolder)}
     * and / or {@link #getDragDirs(RecyclerView, ViewHolder)}.
     *
     * @param dragDirs  Binary OR of direction flags in which the Views can be dragged. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     * @param swipeDirs Binary OR of direction flags in which the Views can be swiped. Must be
     *                  composed of {@link #LEFT}, {@link #RIGHT}, {@link #START}, {@link
     *                  #END},
     *                  {@link #UP} and {@link #DOWN}.
     */
    public LeftRightCallback(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
        padding = 50;
    }

    /**
     * Specify icon and a background that will be drawn beneath swipe item during the left swipe
     * and a listener, that will be called.
     *
     * @param icon icon to draw on the right side of the item
     * @param background background to draw beneath the item
     * @param onSwipeListener listener to call on swipe
     */
    public void setLeftSwipe(Drawable icon, ColorDrawable background, OnSwipeListener onSwipeListener) {
        leftSwipeIcon = icon;
        leftSwipeBackground = background;
        leftSwipeListener = onSwipeListener;
    }

    /**
     * Specify icon and a background that will be drawn beneath swipe item during the right swipe
     * and a listener, that will be called.
     *
     * @param icon icon to draw on the left side of the item
     * @param background background to draw beneath the item
     * @param onSwipeListener listener to call on swipe
     */
    public void setRightSwipe(Drawable icon, ColorDrawable background, OnSwipeListener onSwipeListener) {
        rightSwipeIcon = icon;
        rightSwipeBackground = background;
        rightSwipeListener = onSwipeListener;
    }

    /**
     * Set the padding of the icon, drawn during the swipe.
     *
     * @param padding padding of the icon
     */
    public void setPadding(int padding) {
        this.padding = padding;
    }

    /**
     * This implementation does not support moving items in recycler view.
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View item = viewHolder.itemView;
        int size = item.getBottom() - item.getTop();
        if (dX > 0 && rightSwipeIcon != null && rightSwipeBackground != null) {
            rightSwipeBackground.setBounds(item.getLeft(), item.getTop(), item.getRight(), item.getBottom());
            rightSwipeIcon.setBounds(item.getLeft() + padding, item.getTop() + padding, item.getLeft() + size - padding, item.getBottom() - padding);
            rightSwipeBackground.draw(c);
            rightSwipeIcon.draw(c);
        } else if (dX < 0 && leftSwipeIcon != null && leftSwipeBackground != null) {
            leftSwipeBackground.setBounds(item.getLeft(), item.getTop(), item.getRight(), item.getBottom());
            leftSwipeIcon.setBounds(item.getRight() - size + padding, item.getTop() + padding, item.getRight() - padding, item.getBottom() - padding);
            leftSwipeBackground.draw(c);
            leftSwipeIcon.draw(c);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.LEFT && leftSwipeListener != null) {
            leftSwipeListener.onSwipe(viewHolder);
        } else if (direction == ItemTouchHelper.RIGHT && rightSwipeListener != null) {
            rightSwipeListener.onSwipe(viewHolder);
        }
    }

    /**
     * Listener of swipe events.
     */
    public interface OnSwipeListener {

        /**
         * Callback, called when the swipe over specified view holder occurs.
         *
         * @param viewHolder view holder, over which the swipe has occurred
         */
        void onSwipe(RecyclerView.ViewHolder viewHolder);
    }

}
