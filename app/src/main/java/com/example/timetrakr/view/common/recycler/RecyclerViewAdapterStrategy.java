package com.example.timetrakr.view.common.recycler;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Strategy used by RecyclerViewAdapter to properly update a RecyclerView.
 */
public interface RecyclerViewAdapterStrategy {
    /**
     * Figure out difference between the old data set and the new data set and convey it
     * to the RecyclerViewAdapter.
     *
     * @param adapterToNotify recycler view adapter to notify about a data set change
     * @param oldDataSet old version of a data set
     * @param newDataSet new version of a data set
     */
    void notifyDataSetChanged(RecyclerView.Adapter adapterToNotify, List oldDataSet, List newDataSet);
}
