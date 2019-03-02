package com.example.timetrakr.view.common.recycler;

import android.view.View;
import android.widget.TextView;

import com.example.timetrakr.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class EmptiableRecyclerViewTest {

    private EmptiableRecyclerView<Object> recyclerView;
    private RecyclerView actualRecyclerView;
    private TextView noItemsPlaceHolderView;
    private ListAdapter<Object, ?> listAdapter;

    @Before
    public void setUp() throws Exception {
        actualRecyclerView = Mockito.mock(RecyclerView.class);
        noItemsPlaceHolderView = Mockito.mock(TextView.class);
        recyclerView = Mockito.mock(EmptiableRecyclerView.class);
        Mockito.doCallRealMethod().when(recyclerView).initialize(Mockito.any(), Mockito.any());
        Mockito.doCallRealMethod().when(recyclerView).setLayoutManager(Mockito.any());
        Mockito.doCallRealMethod().when(recyclerView).setListAdapter(Mockito.any());
        Mockito.doCallRealMethod().when(recyclerView).setItemTouchHelper(Mockito.any());
        Mockito.doCallRealMethod().when(recyclerView).displayList(Mockito.any());
        Mockito.doCallRealMethod().when(recyclerView).setEmptyListPlaceholderText(Mockito.anyString());
        Mockito.when(recyclerView.findViewById(R.id.recycler_view)).thenReturn(actualRecyclerView);
        Mockito.when(recyclerView.findViewById(R.id.empty_recycler_view_placeholder)).thenReturn(noItemsPlaceHolderView);
        recyclerView.initialize(null, null);
        listAdapter = Mockito.mock(ListAdapter.class);
        recyclerView.setListAdapter(listAdapter);
    }

    @Test
    public void setLayoutManager() {
        RecyclerView.LayoutManager layoutManager = Mockito.mock(RecyclerView.LayoutManager.class);
        recyclerView.setLayoutManager(layoutManager);
        Mockito.verify(actualRecyclerView).setLayoutManager(layoutManager);
    }

    @Test(expected = IllegalStateException.class)
    public void displayListWithoutAdapter() {
        recyclerView.setListAdapter(null);
        recyclerView.displayList(Collections.emptyList());
    }

    @Test
    public void displayNonEmptyList() {
        List<Object> objectList = Collections.singletonList(new Object());
        recyclerView.displayList(objectList);
        Mockito.verify(actualRecyclerView).setVisibility(View.VISIBLE);
        Mockito.verify(noItemsPlaceHolderView).setVisibility(View.GONE);
        Mockito.verify(listAdapter).displayList(objectList);
    }

    @Test
    public void displayEmptyList() {
        List<Object> objectList = Collections.emptyList();
        recyclerView.displayList(objectList);
        Mockito.verify(actualRecyclerView).setVisibility(View.GONE);
        Mockito.verify(noItemsPlaceHolderView).setVisibility(View.VISIBLE);
        Mockito.verify(listAdapter).displayList(objectList);
    }

    @Test
    public void setListAdapter() {
        Mockito.reset(actualRecyclerView);
        recyclerView.setListAdapter(listAdapter);
        Mockito.verify(actualRecyclerView).setAdapter(listAdapter);
    }

    @Test
    public void setItemTouchHelper() {
        ItemTouchHelper itemTouchHelper = Mockito.mock(ItemTouchHelper.class);
        recyclerView.setItemTouchHelper(itemTouchHelper);
        Mockito.verify(itemTouchHelper).attachToRecyclerView(actualRecyclerView);
    }

    @Test
    public void setEmptyListPlaceholderText() {
        String text = "The list is empty.";
        recyclerView.setEmptyListPlaceholderText(text);
        Mockito.verify(noItemsPlaceHolderView).setText(text);
    }

}
