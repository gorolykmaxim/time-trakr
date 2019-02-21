package com.example.timetrakr.view.activities.started.dialog;

import java.time.format.DateTimeFormatter;

/**
 * Factory of {@link StartActivityDialogFragment} instances.
 */
public class StartActivityDialogDisplayerFactory {

    private DateTimeFormatter formatter;

    /**
     * Construct a factory.
     *
     * @param formatter formatter used to display a start date-time in the create dialogs
     */
    public StartActivityDialogDisplayerFactory(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * Create activity start dialog
     * @return created activity start dialog
     */
    public StartActivityDialogFragment create() {
        return StartActivityDialogFragment.create(formatter);
    }

}
