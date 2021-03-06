package com.example.timetrakr.model.messages.common;

import com.example.timetrakr.model.messages.Condition;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class OrTest {

    @Test
    public void test() {
        Object object = new Object();
        Condition<Object> conditionA = Mockito.mock(Condition.class);
        Condition<Object> conditionB = Mockito.mock(Condition.class);
        Condition<Object> condition = new Or<>(conditionA, conditionB);
        // Should apply if both conditions apply.
        Mockito.when(conditionA.appliesTo(object)).thenReturn(true);
        Mockito.when(conditionB.appliesTo(object)).thenReturn(true);
        Assert.assertTrue(condition.appliesTo(object));
        // Should apply if second condition applies.
        Mockito.when(conditionA.appliesTo(object)).thenReturn(false);
        Mockito.when(conditionB.appliesTo(object)).thenReturn(true);
        Assert.assertTrue(condition.appliesTo(object));
        // Should apply if first condition applies.
        Mockito.when(conditionA.appliesTo(object)).thenReturn(true);
        Mockito.when(conditionB.appliesTo(object)).thenReturn(false);
        Assert.assertTrue(condition.appliesTo(object));
        // Should not apply if both conditions do not apply.
        Mockito.when(conditionA.appliesTo(object)).thenReturn(false);
        Mockito.when(conditionB.appliesTo(object)).thenReturn(false);
        Assert.assertFalse(condition.appliesTo(object));
    }

}
