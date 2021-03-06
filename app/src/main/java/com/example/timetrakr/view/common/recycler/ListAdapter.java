package com.example.timetrakr.view.common.recycler;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.Adapter} that displays list of entities. This could've been a simple
 * interface, but android SDK is designed in such a way, that recycler view adapter is an abstract
 * class rather than an interface.
 *
 * @param <T> entity type
 * @param <VH> {@link RecyclerView.ViewHolder} type
 */
public abstract class ListAdapter<T, VH extends RecyclerView.ViewHolder> extends androidx.recyclerview.widget.ListAdapter<T, VH> {
    /**
     * {@inheritDoc}
     */
    protected ListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    /**
     * Display specified list in the {@link RecyclerView}, to which this adapter is attached.
     *
     * @param list list of entities to display
     */
    public abstract void displayList(List<T> list);

}
