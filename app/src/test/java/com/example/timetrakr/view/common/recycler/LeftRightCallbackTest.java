package com.example.timetrakr.view.common.recycler;

import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RecyclerView.ViewHolder.class})
public class LeftRightCallbackTest {

    private Drawable icon;
    private ColorDrawable background;
    private LeftRightCallback.OnSwipeListener listener;
    private RecyclerView.ViewHolder viewHolder;
    private View itemView;
    private Canvas canvas;
    private RecyclerView recyclerView;
    private int size;

    @Before
    public void setUp() throws Exception {
        icon = Mockito.mock(Drawable.class);
        background = Mockito.mock(ColorDrawable.class);
        listener = Mockito.mock(LeftRightCallback.OnSwipeListener.class);
        viewHolder = PowerMockito.mock(RecyclerView.ViewHolder.class);
        itemView = Mockito.mock(View.class);
        Mockito.when(itemView.getLeft()).thenReturn(0);
        Mockito.when(itemView.getTop()).thenReturn(0);
        Mockito.when(itemView.getRight()).thenReturn(400);
        Mockito.when(itemView.getBottom()).thenReturn(200);
        size = 200;
        MemberModifier.field(RecyclerView.ViewHolder.class, "itemView").set(viewHolder, itemView);
        canvas = Mockito.mock(Canvas.class);
        recyclerView = Mockito.mock(RecyclerView.class);
    }

    @Test
    public void rightSwipeWithLeftSwipeListener() {
        LeftRightCallback callback = new LeftRightCallback(0, ItemTouchHelper.LEFT);
        callback.setLeftSwipe(icon, background, listener);
        // Should be no reaction to swipe to the right.
        callback.onChildDraw(canvas, recyclerView, viewHolder, 5, 0, 0, false);
        Mockito.verify(icon, Mockito.never()).draw(canvas);
        Mockito.verify(background, Mockito.never()).draw(canvas);
        callback.onSwiped(viewHolder, ItemTouchHelper.RIGHT);
        Mockito.verify(listener, Mockito.never()).onSwipe(viewHolder);
    }

    @Test
    public void leftSwipeDefaultPadding() {
        LeftRightCallback callback = new LeftRightCallback(0, ItemTouchHelper.LEFT);
        leftSwipe(callback, 50);
    }

    @Test
    public void leftSwipeCustomPadding() {
        LeftRightCallback callback = new LeftRightCallback(0, ItemTouchHelper.LEFT);
        callback.setPadding(35);
        leftSwipe(callback, 35);
    }

    private void leftSwipe(LeftRightCallback callback, int padding) {
        callback.setLeftSwipe(icon, background, listener);
        callback.onChildDraw(canvas, recyclerView, viewHolder, -5, 0, 0, false);
        InOrder order = Mockito.inOrder(icon, background);
        order.verify(background).setBounds(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
        order.verify(icon).setBounds(itemView.getRight() - size + padding, itemView.getTop() + padding, itemView.getRight() - padding, itemView.getBottom() - padding);
        order.verify(background).draw(canvas);
        order.verify(icon).draw(canvas);
        callback.onSwiped(viewHolder, ItemTouchHelper.LEFT);
        Mockito.verify(listener).onSwipe(viewHolder);
    }

    @Test
    public void rightSwipeDefaultPadding() {
        LeftRightCallback callback = new LeftRightCallback(0, ItemTouchHelper.RIGHT);
        rightSwipe(callback, 50);
    }

    @Test
    public void rightSwipeCustomPadding() {
        LeftRightCallback callback = new LeftRightCallback(0, ItemTouchHelper.RIGHT);
        callback.setPadding(35);
        rightSwipe(callback, 35);
    }

    private void rightSwipe(LeftRightCallback callback, int padding) {
        callback.setRightSwipe(icon, background, listener);
        callback.onChildDraw(canvas, recyclerView, viewHolder, 5, 0, 0, false);
        InOrder order = Mockito.inOrder(icon, background);
        order.verify(background).setBounds(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
        order.verify(icon).setBounds(itemView.getLeft() + padding, itemView.getTop() + padding, itemView.getLeft() + size - padding, itemView.getBottom() - padding);
        order.verify(background).draw(canvas);
        order.verify(icon).draw(canvas);
        callback.onSwiped(viewHolder, ItemTouchHelper.RIGHT);
        Mockito.verify(listener).onSwipe(viewHolder);
    }

    @Test
    public void leftSwipeWithRightSwipeListener() {
        LeftRightCallback callback = new LeftRightCallback(0, ItemTouchHelper.RIGHT);
        callback.setRightSwipe(icon, background, listener);
        // Should be no reaction to swipe to the left.
        callback.onChildDraw(canvas, recyclerView, viewHolder, -5, 0, 0, false);
        Mockito.verify(icon, Mockito.never()).draw(canvas);
        Mockito.verify(background, Mockito.never()).draw(canvas);
        callback.onSwiped(viewHolder, ItemTouchHelper.LEFT);
        Mockito.verify(listener, Mockito.never()).onSwipe(viewHolder);
    }
}
