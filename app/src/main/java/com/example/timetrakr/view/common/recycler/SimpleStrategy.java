package com.example.timetrakr.view.common.recycler;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Simple strategy that updates entire RecyclerView no matter the difference between data sets.
 */
public class SimpleStrategy implements RecyclerViewAdapterStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged(RecyclerView.Adapter adapterToNotify, List oldDataSet, List newDataSet) {
        adapterToNotify.notifyDataSetChanged();
    }

}
