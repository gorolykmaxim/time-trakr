package com.example.timetrakr.view.common.dialog;

/**
 * Factory to create {@link DismissableDialog} instances.
 *
 * @param <T> concrete type of the created dialog
 */
public interface DismissableDialogFactory <T extends DismissableDialog> {

    /**
     * Create a dialog.
     *
     * @return created dialog
     */
    T create();

}
