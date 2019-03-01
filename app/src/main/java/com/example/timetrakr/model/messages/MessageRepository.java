package com.example.timetrakr.model.messages;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Keeps messages, that can be shown to the user. Each message has a specific condition, that
 * should be satisfied by the specified input data in order for its message to be returned.
 * More than one message can be bind to one set of conditions. Each message may have variable parts,
 * that are built based of specified input data.
 */
public class MessageRepository <T> {

    private Deque<MessagesContainer> messagesContainers;
    private Random random;

    /**
     * Construct message repository.
     */
    public MessageRepository() {
        messagesContainers = new ArrayDeque<>();
        random = new Random();
    }

    /**
     * Find a message, condition of which is satisfied by the specified input data. In case message
     * has variable parts - build it based of specified input data.
     *
     * @param t input data
     * @return message, condition of which has been satisfied. Can never be null. In case no message
     * where found, a {@link NoMessageFoundError} will be thrown.
     */
    public String findOneThatAppliesTo(T t) {
        for (MessagesContainer messagesContainer: messagesContainers) {
            if (messagesContainer.doesConditionAppliesTo(t)) {
                return messagesContainer.buildMessageFor(t);
            }
        }
        throw new NoMessageFoundError(t);
    }

    /**
     * Save specified array of message in the repository. When looking for a message in this
     * repository any of those messages may be returned in case specified condition applies
     * to the specified input data. Save messages that have more broad condition (e.g. condition
     * that can be met for often) first and messages, that have a more specific condition - last.
     * Always populate the repository in such a way, so that there is always at least one message,
     * condition of which meets any input data.
     *
     * @param condition condition that has to be met by the input data in order for one of
     *                  the specified messages to be returned by the repository
     * @param messages array of messages, each of which can be displayed when the specified
     *                 condition is met by the input data
     */
    public void save(Condition<T> condition, Message<T>... messages) {
        messagesContainers.push(new MessagesContainer(condition, Arrays.asList(messages), random));
    }

    /**
     * A pair of a condition and a collection of messages, each of which can be displayed
     * if the specified condition applies to the specified input data.
     */
    private class MessagesContainer {

        private Condition<T> condition;
        private List<Message<T>> messages;
        private Random random;

        /**
         * Construct a message container.
         *
         * @param condition condition to bind messages to
         * @param messages messages, one of which should be displayed if the condition is met
         * @param random random number generator to pick a random message from the specified list
         */
        public MessagesContainer(Condition<T> condition, List<Message<T>> messages, Random random) {
            this.condition = condition;
            this.messages = messages;
            this.random = random;
        }

        /**
         * Check if bound condition does applies to the specified input data.
         *
         * @param t input data to check
         * @return true if condition applies to the specified input data
         */
        public boolean doesConditionAppliesTo(T t) {
            return condition.appliesTo(t);
        }

        /**
         * Pick a random message from those that were bound to this container and build it
         * according to the specified input data.
         *
         * @param t input data
         * @return final representation of the message
         */
        public String buildMessageFor(T t) {
            Message<T> message = messages.get(random.nextInt(messages.size()));
            return message.buildFor(t);
        }

    }

}
