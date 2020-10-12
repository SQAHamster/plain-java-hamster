package de.unistuttgart.iste.rss.oo.hamstersimulator.adapter;

import java.util.Optional;

/**
 * Interface to interact with a UI or mock object for reading values from the user.
 * @author Steffen Becker
 *
 */
public interface InputInterface {

    /**
     * Read an integer value from a user. This blocks until there is
     * an integer to return or it is aborted
     * @param message The message used in the prompt for the number.
     * @return The integer value read or an empty optional, if aborted.
     */
    Optional<Integer> readInteger(String message);

    /**
     * Read a string value from a user. This blocks until there is a
     * String to return or it is aborted
     * @param message The message used in the prompt for the string.
     * @return The string value read or an empty optional, if aborted.
     */
    Optional<String> readString(String message);

    /**
     * Inform a user about an abnormal execution aborting.
     * This blocks until it returns or is aborted
     * @param t The throwable which lead to aborting the program.
     */
    void showAlert(Throwable t);

    /**
     * Aborts readInteger, readString or showAlert. This is used due to multi ui systems,
     * where every dialog could return first. Because only one dialog is able to return a
     * value, all other dialogs can be aborted.<br>
     * This must be thread-safe
     */
    void abort();
}