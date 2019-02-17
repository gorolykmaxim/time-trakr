package com.example.timetrakr.view.common.recycler;

import androidx.recyclerview.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RecyclerView.Adapter.class})
public class SimpleStrategyTest {

    private RecyclerView.Adapter adapter;
    private RecyclerViewAdapterStrategy strategy;

    @Before
    public void setUp() throws Exception {
        adapter = PowerMockito.mock(RecyclerView.Adapter.class);
        strategy = new SimpleStrategy();
    }

    @Test
    public void equalLists() {
        strategy.notifyDataSetChanged(adapter, Collections.emptyList(), Collections.emptyList());
        Mockito.verify(adapter).notifyDataSetChanged();
    }

    @Test
    public void differentLists() {
        strategy.notifyDataSetChanged(adapter, Collections.emptyList(), Collections.singletonList(new Object()));
        Mockito.verify(adapter).notifyDataSetChanged();
    }
}
