package com.example.timetrakr.viewmodel.common;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Executor of tasks, that runs them inside {@link android.os.AsyncTask} instances.
 */
public class AsyncTaskExecutor {

    private boolean executeOnCurrentThread;

    /**
     * Construct the executor.
     */
    public AsyncTaskExecutor() {
        this.executeOnCurrentThread = false;
    }

    /**
     * Make executor execute specified tasks on the current thread,
     * bypassing most of {@link android.os.AsyncTask} thread validations.
     *
     * <p>This should be enabled <b>ONLY</b> while unit testing the executor. It is the sole reason
     * of this method existing. Never enable this in the actual application.</p>
     */
    void executeTasksOnCurrentThread() {
        executeOnCurrentThread = true;
    }

    /**
     * Execute specified task without listeners of it's result.
     *
     * @param task task to execute
     */
    public <V> void execute(Supplier<V> task) {
        executeTask(new DelegatingAsyncTask<>(task));
    }

    /**
     * Execute specified task and pass it's results to the specified listener.
     *
     * @param task task to execute
     * @param listener listener to pass execution results to
     */
    public <V> void execute(Supplier<V> task, Consumer<V> listener) {
        executeTask(new DelegatingAsyncTask<>(task, listener));
    }

    /**
     * Execute specified async task either normally, or emulate its execution on current thread.
     *
     * @param asyncTask async task to execute
     */
    private<V> void executeTask(DelegatingAsyncTask<V> asyncTask) {
        if (executeOnCurrentThread) {
            // Hack to make AsyncTask unit-testable:
            // both execute() and executeOnExecutor() check the thread they are being called on
            // and if it is not a UI thread (which is not in case of unit tests) - they fail.
            // This will emulate AsyncTask behavior close enough, to test the actual AsyncTask.
            asyncTask.onPostExecute(asyncTask.doInBackground());
        } else {
            asyncTask.execute();
        }
    }

}
