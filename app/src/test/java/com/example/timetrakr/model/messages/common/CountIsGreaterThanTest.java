package com.example.timetrakr.model.messages.common;

import com.example.timetrakr.model.messages.Condition;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CountIsGreaterThanTest {

    @Test
    public void test() {
        Condition<List<Object>> condition = new CountIsGreaterThan<>(1);
        // Condition should apply to the list with a size greater than the specified one.
        List<Object> objectList = Arrays.asList(new Object(), new Object());
        Assert.assertTrue(condition.appliesTo(objectList));
        // Condition should not apply to the list with a size equal to the specified one.
        objectList = Collections.singletonList(new Object());
        Assert.assertFalse(condition.appliesTo(objectList));
        // Condition should not apply to the list with a size smaller than the specified one.
        objectList = Collections.emptyList();
        Assert.assertFalse(condition.appliesTo(objectList));
    }

}
