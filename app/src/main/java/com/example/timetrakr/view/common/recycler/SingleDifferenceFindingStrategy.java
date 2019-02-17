package com.example.timetrakr.view.common.recycler;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * In case the difference between old and new data sets is one element - find its position,
 * determine if it was added or removed and notify RecyclerView about it.
 * Otherwise update entire RecyclerView.
 */
public class SingleDifferenceFindingStrategy implements RecyclerViewAdapterStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged(RecyclerView.Adapter adapterToNotify, List oldDataSet, List newDataSet) {
        if (Math.abs(oldDataSet.size() - newDataSet.size()) != 1) {
            adapterToNotify.notifyDataSetChanged();
        } else if (oldDataSet.size() > newDataSet.size()) {
            for (int i = 0; i < oldDataSet.size(); i++) {
                if (!newDataSet.contains(oldDataSet.get(i))) {
                    adapterToNotify.notifyItemRemoved(i);
                    break;
                }
            }
        } else {
            for (int i = 0; i < newDataSet.size(); i++) {
                if (!oldDataSet.contains(newDataSet.get(i))) {
                    adapterToNotify.notifyItemInserted(i);
                    break;
                }
            }
        }
    }
}
