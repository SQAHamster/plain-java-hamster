package de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes;

import de.unistuttgart.iste.rss.oo.hamstersimulator.internal.model.hamster.GameHamster;
import de.unistuttgart.iste.rss.utils.Preconditions;

/**
 * A log entry object represents a log message of the
 * hamster simulator and is typically displayed in the log
 * view at the right side of the simulator window.
 * @author Steffen Becker
 *
 */
public class LogEntry {

    /**
     * Immutable message of this log entry.
     */
    private final String message;

    /**
     * Immutable reference to the hamster which logged that entry.
     */
    private final GameHamster hamster;

    /** Create a new log entry.
     * @param loggingHamster Reference to the hamster which created the log message.
     *        Must not be null and the hamster has to be initialized
     * @param loggedMessage The message to be logged. Has not to be null and not to be empty.
     */
    public LogEntry(final GameHamster loggingHamster, final String loggedMessage) {
        super();
        Preconditions.checkNotNull(loggingHamster);
        Preconditions.checkNotNull(loggedMessage);
        Preconditions.checkArgument(!loggedMessage.equals(""), "Empty message not allowed");
        this.hamster = loggingHamster;
        this.message = loggedMessage;
    }

    /**
     * @return Return the hamster reference which logged this entry
     */
    public GameHamster getHamster() {
        return hamster;
    }

    /**
     * @return The log message to be displayed
     */
    public String getMessage() {
        return message;
    }
}
