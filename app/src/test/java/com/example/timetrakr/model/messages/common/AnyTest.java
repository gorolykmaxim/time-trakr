package com.example.timetrakr.model.messages.common;

import org.junit.Assert;
import org.junit.Test;

public class AnyTest {

    @Test
    public void test() {
        Assert.assertTrue(new Any<>().appliesTo(new Object()));
    }

}
