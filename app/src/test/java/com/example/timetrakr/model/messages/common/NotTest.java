package com.example.timetrakr.model.messages.common;

import com.example.timetrakr.model.messages.Condition;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class NotTest {

    @Test
    public void test() {
        Object object = new Object();
        Condition<Object> conditionToInvert = Mockito.mock(Condition.class);
        Condition<Object> condition = new Not<>(conditionToInvert);
        // When specified condition applies to object, not condition should not apply.
        Mockito.when(conditionToInvert.appliesTo(object)).thenReturn(true);
        Assert.assertFalse(condition.appliesTo(object));
        // When specified condition does not apply to object, not condition should apply.
        Mockito.when(conditionToInvert.appliesTo(object)).thenReturn(false);
        Assert.assertTrue(condition.appliesTo(object));
    }

}
