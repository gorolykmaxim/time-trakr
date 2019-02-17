package com.example.timetrakr.viewmodel.common;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AsyncTaskExecutorTest {

    private AsyncTaskExecutor executor;
    private Consumer<Object> resultListener;
    private Supplier<Object> task;

    @Before
    public void setUp() throws Exception {
        executor = new AsyncTaskExecutor();
        executor.executeTasksOnCurrentThread();
        resultListener = Mockito.mock(Consumer.class);
        task = Mockito.mock(Supplier.class);
    }

    @Test
    public void executeWithResultAndListener() {
        Object result = new Object();
        Mockito.when(task.get()).thenReturn(result);
        executor.execute(task, resultListener);
        Mockito.verify(resultListener).accept(result);
    }

    @Test
    public void executeWithoutResultWithListener() {
        executor.execute(task, resultListener);
        Mockito.verify(task).get();
        Mockito.verify(resultListener, Mockito.never()).accept(Mockito.any());
    }

    @Test
    public void executeWithoutListenerWithResult() {
        Mockito.when(task.get()).thenReturn(new Object());
        executor.execute(task);
        Mockito.verify(task).get();
    }
}
