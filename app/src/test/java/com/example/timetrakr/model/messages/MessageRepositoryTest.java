package com.example.timetrakr.model.messages;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MessageRepositoryTest {

    private MessageRepository<Object> repository;
    private Condition<Object> easyCondition, mediumCondition, hardCondition;
    private String easyMessage, mediumMessage1, mediumMessage2, hardMessage;
    private PartBuilder<Object> mediumMessage2Builder, hardMessageBuilder1, hardMessageBuilder2;
    private Object inputData;

    @Before
    public void setUp() throws Exception {
        inputData = new Object();
        repository = new MessageRepository<>();
        easyCondition = Mockito.mock(Condition.class);
        Mockito.when(easyCondition.appliesTo(inputData)).thenReturn(false);
        mediumCondition = Mockito.mock(Condition.class);
        Mockito.when(mediumCondition.appliesTo(inputData)).thenReturn(false);
        hardCondition = Mockito.mock(Condition.class);
        Mockito.when(hardCondition.appliesTo(inputData)).thenReturn(false);
        easyMessage = "Easy message";
        mediumMessage1 = "Medium message";
        mediumMessage2 = "%s medium message";
        hardMessage = "%s hard %s message";
        mediumMessage2Builder = Mockito.mock(PartBuilder.class);
        hardMessageBuilder1 = Mockito.mock(PartBuilder.class);
        hardMessageBuilder2 = Mockito.mock(PartBuilder.class);
        // Register messages in the repository.
        // First save the most common message.
        repository.save(easyCondition, new Message<>(easyMessage));
        // Second save messages, that are usually shown.
        repository.save(mediumCondition, new Message<>(mediumMessage1), new Message<>(mediumMessage2, mediumMessage2Builder));
        // Last save the message with the most specific condition.
        repository.save(hardCondition, new Message<>(hardMessage, hardMessageBuilder1, hardMessageBuilder2));
    }

    @Test
    public void findHardMessage() {
        Mockito.when(hardCondition.appliesTo(inputData)).thenReturn(true);
        Mockito.when(hardMessageBuilder1.buildFrom(inputData)).thenReturn("Actually");
        Mockito.when(hardMessageBuilder2.buildFrom(inputData)).thenReturn("freaking");
        String message = repository.findOneThatAppliesTo(inputData);
        Assert.assertEquals("Actually hard freaking message", message);
    }

    @Test
    public void findMediumMessage() {
        Mockito.when(mediumCondition.appliesTo(inputData)).thenReturn(true);
        Mockito.when(mediumMessage2Builder.buildFrom(inputData)).thenReturn("Not so");
        String message = repository.findOneThatAppliesTo(inputData);
        Assert.assertTrue(message.equals("Medium message") || message.equals("Not so medium message"));
    }

    @Test
    public void findEasyMessage() {
        Mockito.when(easyCondition.appliesTo(inputData)).thenReturn(true);
        String message = repository.findOneThatAppliesTo(inputData);
        Assert.assertEquals(easyMessage, message);
    }

    @Test
    public void findHardMessageWhenEasyAlsoApplies() {
        Mockito.when(hardCondition.appliesTo(inputData)).thenReturn(true);
        Mockito.when(hardMessageBuilder1.buildFrom(inputData)).thenReturn("Actually");
        Mockito.when(hardMessageBuilder2.buildFrom(inputData)).thenReturn("freaking");
        Mockito.when(easyCondition.appliesTo(inputData)).thenReturn(true);
        String message = repository.findOneThatAppliesTo(inputData);
        Assert.assertEquals("Actually hard freaking message", message);
    }

    @Test(expected = NoMessageFoundError.class)
    public void findNoMessages() {
        repository.findOneThatAppliesTo(inputData);
    }
}
