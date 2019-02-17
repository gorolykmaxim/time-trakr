package com.example.timetrakr.view.common.recycler;

import androidx.recyclerview.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RecyclerView.Adapter.class})
public class SingleDifferenceFindingStrategyTest {

    private RecyclerView.Adapter adapter;
    private RecyclerViewAdapterStrategy strategy;

    @Before
    public void setUp() throws Exception {
        adapter = PowerMockito.mock(RecyclerView.Adapter.class);
        strategy = new SingleDifferenceFindingStrategy();
    }

    @Test
    public void dataSetsHaveEqualLength() {
        strategy.notifyDataSetChanged(adapter, Collections.singletonList(new Object()), Collections.singletonList(new Object()));
        Mockito.verify(adapter).notifyDataSetChanged();
    }

    @Test
    public void dataSetsHaveTooManyDifferentElements() {
        List<Object> oldDataSet = Arrays.asList(new Object(), new Object(), new Object(), new Object());
        List<Object> newDataSet = Collections.singletonList(new Object());
        strategy.notifyDataSetChanged(adapter, oldDataSet, newDataSet);
        Mockito.verify(adapter).notifyDataSetChanged();
    }

    @Test
    public void oneItemAdded() {
        Object commonObject = new Object();
        List<Object> oldDataSet = Collections.singletonList(commonObject);
        List<Object> newDataSet = Arrays.asList(commonObject, new Object());
        strategy.notifyDataSetChanged(adapter, oldDataSet, newDataSet);
        Mockito.verify(adapter).notifyItemInserted(1);
    }

    @Test
    public void oneItemRemoved() {
        Object commonObject = new Object();
        List<Object> oldDataSet = Arrays.asList(commonObject, new Object());
        List<Object> newDataSet = Collections.singletonList(commonObject);
        strategy.notifyDataSetChanged(adapter, oldDataSet, newDataSet);
        Mockito.verify(adapter).notifyItemRemoved(1);
    }

}
