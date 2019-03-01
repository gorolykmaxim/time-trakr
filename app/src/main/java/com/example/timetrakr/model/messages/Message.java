package com.example.timetrakr.model.messages;

/**
 * Message to display to the user, that might contain variable parts.
 */
public class Message <T> {

    private String template;
    private PartBuilder<T>[] partBuilders;

    /**
     * Construct a message.
     *
     * @param template usual java string template, to build message based of
     * @param partBuilders part builders to build variable parts of the message with. In case
     *                     of a static message, there is no need in specifying any.
     */
    public Message(String template, PartBuilder<T>... partBuilders) {
        this.template = template;
        this.partBuilders = partBuilders;
    }

    /**
     * Build message, that corresponds to the specified input data.
     *
     * @param t input data
     * @return built message
     */
    public String buildFor(T t) {
        String[] variableParts = new String[partBuilders.length];
        for (int i = 0; i < partBuilders.length; i++) {
            variableParts[i] = partBuilders[i].buildFrom(t);
        }
        return String.format(template, variableParts);
    }

}
