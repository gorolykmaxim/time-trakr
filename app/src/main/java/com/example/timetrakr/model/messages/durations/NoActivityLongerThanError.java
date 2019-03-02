package com.example.timetrakr.model.messages.durations;

import java.time.Duration;

/**
 * You have miss-configured a message condition. Either:
 * <ul>
 *     <li>
 *         a) duration specified in the condition of this message is not the same as the
 *         duration, specified in this builder.
 *     </li>
 *     <li>
 *         b) you haven't bound the message, that uses this builder, with a condition, that
 *         checks that there is an activity with a duration longer than the duration,
 *         specified in this builder.
 *     </li>
 * </ul>
 */
public class NoActivityLongerThanError extends RuntimeException {

    /**
     * Construct the error.
     *
     * @param duration there were no activity with a duration longer then this one
     */
    public NoActivityLongerThanError(Duration duration) {
        super(String.format("There is no activity with duration longer than %s. " +
                "Check conditions, you are using this builder with. " +
                "You should be using this builder in pair with conditions like %s.", duration, HasDurationLongerThan.class.getName()));
    }

}
