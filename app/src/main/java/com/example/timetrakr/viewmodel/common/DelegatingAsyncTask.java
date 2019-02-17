package com.example.timetrakr.viewmodel.common;

import android.os.AsyncTask;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * {@link AsyncTask} that can execute specified {@link Supplier} and it's result
 * to the specified {@link Consumer}.
 *
 * @param <V> type of the result, produced by the {@link Supplier} and consumed by the {@link Consumer}
 */
public class DelegatingAsyncTask<V> extends AsyncTask<Void, Void, V> {

    private Supplier<V> task;
    private Consumer<V> listener;

    /**
     * Construct async task without result listener.
     *
     * @param task task to execute
     */
    public DelegatingAsyncTask(Supplier<V> task) {
        this.task = task;
    }

    /**
     * Construct async task with result listner.
     *
     * @param task task to execute
     * @param listener listener to pass results of execution to
     */
    public DelegatingAsyncTask(Supplier<V> task, Consumer<V> listener) {
        this.task = task;
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected V doInBackground(Void... voids) {
        return task.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostExecute(V v) {
        if (v != null && listener != null) {
            listener.accept(v);
        }
    }

}
