package com.example.timetrakr.view.common.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.timetrakr.R;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Pseudo recycler view, that displays list of entities, which can be empty. In case currently
 * displayed list of entities is empty - specified placeholder message is displayed in a text view
 * instead of an actual recycler view.
 *
 * @param <T> entity type
 */
public class EmptiableRecyclerView<T> extends LinearLayout {

    private RecyclerView recyclerView;
    private TextView noItemsPlaceHolderView;
    private ListAdapter<T, ?> listAdapter;

    /**
     * Construct a recycler view.
     *
     * @param context context in scope of which this recycler view is displayed
     * @param attrs attributes of this view
     */
    public EmptiableRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.emptiable_recycler_view, this);
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
        recyclerView = findViewById(R.id.recycler_view);
        noItemsPlaceHolderView = findViewById(R.id.empty_recycler_view_placeholder);
    }

    /**
     * @see RecyclerView#setLayoutManager(RecyclerView.LayoutManager)
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Basically the same as setAdapter(), but specified argument should be a {@link ListAdapter}.
     *
     * @see RecyclerView#setAdapter(RecyclerView.Adapter)
     */
    public void setListAdapter(ListAdapter<T, ?> listAdapter) {
        recyclerView.setAdapter(listAdapter);
        this.listAdapter = listAdapter;
    }

    /**
     * This is a reversed version of a attachToRecyclerView().
     *
     * @see ItemTouchHelper#attachToRecyclerView(RecyclerView)
     */
    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Display specified list of entities in the recycler view. You need to specify
     * a {@link ListAdapter} using setListAdapter() before calling this method.
     */
    public void displayList(List<T> list) {
        if (listAdapter == null) {
            throw new IllegalStateException(String.format("Specify %s using setListAdapter() before calling this method.", ListAdapter.class.getName()));
        }
        noItemsPlaceHolderView.setVisibility(list.isEmpty() ? VISIBLE : GONE);
        recyclerView.setVisibility(list.isEmpty() ? GONE : VISIBLE);
        listAdapter.displayList(list);
    }

    /**
     * Set the message to be displayed instead of a recycler view, when the list, displayed by
     * the latter one, will be empty.
     *
     * @param placeholderText message to display when displayed list is empty
     */
    public void setEmptyListPlaceholderText(String placeholderText) {
        noItemsPlaceHolderView.setText(placeholderText);
    }

}
