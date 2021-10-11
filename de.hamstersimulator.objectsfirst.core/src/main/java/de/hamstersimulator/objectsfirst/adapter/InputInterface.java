package de.unistuttgart.iste.sqa.oo.hamstersimulator.adapter;

import java.util.Optional;

/**
 * Interface to interact with a UI or mock object for reading values from the user.
 * Warning: subclasses of this interface must support multithreading. <code>readInteger</code>,
 * <code>readString</code> and <code>confirmAlert</code> may be invoked from different threads before
 * the previous invocation returned. It is guaranteed that in this case, <code>abort</code> is called
 * before the next invocation.
 *
 * @author Steffen Becker
 *
 */
public interface
InputInterface {

    /**
     * Read an integer value from a user. This blocks until there is
     * an integer to return or it is aborted.
     * This must be thread safe
     * @param message The message used in the prompt for the number.
     * @return The integer value read or an empty optional, if aborted.
     */
    Optional<Integer> readInteger(String message);

    /**
     * Read a string value from a user. This blocks until there is a
     * String to return or it is aborted
     * This must be thread safe
     * @param message The message used in the prompt for the string.
     * @return The string value read or an empty optional, if aborted.
     */
    Optional<String> readString(String message);

    /**
     * Inform a user about an abnormal execution aborting.
     * This blocks until it returns or is aborted
     * This must be thread safe
     * @param t The throwable which lead to aborting the program.
     */
    void confirmAlert(Throwable t);

    /**
     * Aborts readInteger, readString or confirmAlert. This is used due to multi ui systems,
     * where every dialog could return first. Because only one dialog is able to return a
     * value, all other dialogs can be aborted.<br>
     * This must be thread-safe.
     * May be called multiple times, even if no input is pending
     */
    void abort();
}
